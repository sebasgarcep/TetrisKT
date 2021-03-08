import tornadofx.*

class MainView : View("Tetris") {
    override val root = flowpane {
        style { padding = box(PADDING.px) }
        add<GameAreaView>()
        add<UIAreaView>()
    }
}