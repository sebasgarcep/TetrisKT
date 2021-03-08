import javafx.stage.Stage
import tornadofx.App

class TetrisApp : App(MainView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        stage.height = CANVAS_HEIGHT + 2.0 * PADDING + 30.0
        stage.width = CANVAS_WIDTH + UI_WIDTH + 3.0 * PADDING
        super.start(stage)
    }
}

