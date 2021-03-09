package frontend

import CANVAS_HEIGHT
import CANVAS_WIDTH
import drawBlockGridOnCanvas
import javafx.scene.paint.Color
import tornadofx.*

class GameAreaView : Fragment() {
    override val root = hbox {
        style {
            borderColor = multi(box(Color.BLACK))
            borderWidth = multi(box(2.px))
        }

        canvas {
            height = CANVAS_HEIGHT
            width = CANVAS_WIDTH

            subscribe<GameRefreshEvent> {
                gameRefreshEvent -> run {
                    val blockGrid = gameRefreshEvent.blockGrid
                    drawBlockGridOnCanvas(graphicsContext2D, blockGrid)
                }
            }
        }
    }
}