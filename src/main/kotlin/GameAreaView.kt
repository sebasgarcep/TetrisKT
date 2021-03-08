import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tornadofx.*
import javafx.scene.input.KeyEvent

fun setInterval(ms: Int, callback: () -> Unit): Job {
    return GlobalScope.launch {
        while (true) {
            delay(ms.toLong())
            callback()
        }
    }
}

class RefreshEvent(val blockGrid: BlockGrid) : FXEvent()

class GameAreaView : Fragment() {
    private val gameManager = GameManager()
    init {
        setInterval(1000) {
            val blockGrid = gameManager.tick()
            fire(RefreshEvent(blockGrid))
        }
    }

    override val root = hbox {
        style {
            borderColor = multi(box(Color.BLACK))
            borderWidth = multi(box(2.px))
        }

        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                if (it.code == KeyCode.LEFT) gameManager.tryMoveLeftCurrentPiece()
                if (it.code == KeyCode.RIGHT) gameManager.tryMoveRightCurrentPiece()
                if (it.code == KeyCode.DOWN) gameManager.tryMoveDownCurrentPiece()
                if (it.code == KeyCode.SPACE) gameManager.tryRotateCurrentPiece()
                fire(RefreshEvent(gameManager.getUIBlockGrid()))
            }
        }

        canvas {
            height = CANVAS_HEIGHT
            width = CANVAS_WIDTH

            subscribe<RefreshEvent> {
                refreshEvent -> run {
                    graphicsContext2D.clearRect(0.0, 0.0, CANVAS_WIDTH, CANVAS_HEIGHT)

                    val blockGrid = refreshEvent.blockGrid
                    for (x in 0 until blockGrid.width) {
                        for (y in 0 until blockGrid.height) {
                            val color = blockGrid.get(x, y)
                            if (color == null) continue
                            graphicsContext2D.fill = color
                            graphicsContext2D.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
                        }
                    }
                }
            }
        }
    }
}