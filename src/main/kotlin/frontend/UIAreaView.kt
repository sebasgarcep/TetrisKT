package frontend

import PADDING
import UI_WIDTH
import drawBlockGridOnCanvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.paint.Color
import tornadofx.*

class UIAreaView : Fragment() {
    private val levelLabel = Label("Level: 1")
    private val scoreLabel = Label("Score: 0")
    private val startButton = Button()

    override val root = vbox {
        isFillWidth = true
        style { padding = box(PADDING.px) }

        add(levelLabel)
        add(scoreLabel)

        subscribe<GameRefreshEvent> {
            gameRefreshEvent -> run {
                levelLabel.text = "Level: ${gameRefreshEvent.level}"
                scoreLabel.text = "Score: ${gameRefreshEvent.score}"
            }
        }

        // FIXME: This is a workaround, and we should find a more global way to solve the problem
        startButton.style { focusTraversable = false }
        initStartButtonOnStart()

        add(startButton)

        subscribe<StartEvent> {
            _ -> run {
                initStartButtonOnPause()
            }
        }

        subscribe<PauseEvent> {
            _ -> run {
                initStartButtonOnStart()
            }
        }
    }

    private fun initStartButtonOnStart() {
        startButton.text = "Start"
        startButton.setOnMouseClicked { fire(StartEvent()) }
    }

    private fun initStartButtonOnPause() {
        startButton.text = "Pause"
        startButton.setOnMouseClicked { fire(PauseEvent()) }
    }
}