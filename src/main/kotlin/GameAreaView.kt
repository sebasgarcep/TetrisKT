import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import tornadofx.*
import javafx.scene.input.KeyEvent

class GameAreaViewRefreshEvent(val blockGrid: BlockGrid) : FXEvent()

class GameAreaView : Fragment() {
    override val root = hbox {
        style {
            borderColor = multi(box(Color.BLACK))
            borderWidth = multi(box(2.px))
        }

        canvas {
            height = CANVAS_HEIGHT
            width = CANVAS_WIDTH

            subscribe<GameAreaViewRefreshEvent> {
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