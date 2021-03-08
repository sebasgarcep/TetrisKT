import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import tornadofx.*

class MainView : View("Tetris") {
    private val gameManager = GameManager()
    init {
        setInterval(1000) {
            val blockGrid = gameManager.tick()
            fire(GameAreaViewRefreshEvent(blockGrid))
        }
    }

    override val root = flowpane {
        style { padding = box(PADDING.px) }

        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                if (it.code == KeyCode.LEFT) gameManager.tryMoveLeftCurrentPiece()
                if (it.code == KeyCode.RIGHT) gameManager.tryMoveRightCurrentPiece()
                if (it.code == KeyCode.DOWN) gameManager.tryMoveDownCurrentPiece()
                if (it.code == KeyCode.SPACE) gameManager.tryRotateCurrentPiece()
                fire(GameAreaViewRefreshEvent(gameManager.getUIBlockGrid()))
            }
        }

        add<GameAreaView>()
        add<UIAreaView>()
    }
}