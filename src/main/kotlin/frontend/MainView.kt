package frontend

import PADDING
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import backend.GameManager
import kotlinx.coroutines.Job
import setInterval
import tornadofx.*

class MainView : View("Tetris") {
    private val baseSpeed: Int = 1000
    private var paused = true
    private val gameManager = GameManager()
    private var tickJob: Job? = null

    override val root = flowpane {
        style { padding = box(PADDING.px) }

        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                if (paused) return@addEventHandler
                if (it.code == KeyCode.LEFT) gameManager.tryMoveLeftCurrentPiece()
                if (it.code == KeyCode.RIGHT) gameManager.tryMoveRightCurrentPiece()
                if (it.code == KeyCode.DOWN) gameManager.tryMoveDownCurrentPiece()
                if (it.code == KeyCode.SPACE) gameManager.tryRotateCurrentPiece()
                fire(getGameRefreshEvent())
            }
        }

        add<GameAreaView>()
        add<UIAreaView>()

        fire(getGameRefreshEvent())

        subscribe<StartEvent> {
            resetGameTick()
        }

        subscribe<PauseEvent> {
            stopGameTick()
        }
    }

    private fun getGameRefreshEvent(): GameRefreshEvent {
        val blockGrid = gameManager.getUIBlockGrid()
        val level = gameManager.getLevel()
        val score = gameManager.score
        return GameRefreshEvent(blockGrid, level, score)
    }

    private fun resetGameTick() {
        paused = false
        tickJob?.cancel()
        val level = gameManager.getLevel()
        val speed = baseSpeed * (20 - level + 1) / 20
        tickJob = setInterval(speed) {
            gameManager.tick()
            val event = getGameRefreshEvent()
            if (event.level != level) {
                resetGameTick()
            } else {
                fire(event)
            }
        }
    }

    private fun stopGameTick() {
        paused = true
        tickJob?.cancel()
        tickJob = null
    }
}