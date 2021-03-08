import tornadofx.*

class UIAreaView : Fragment() {
    override val root = vbox {
        isFillWidth = true
        style { padding = box(PADDING.px) }

        label("Level")
        label("Score")
        button("Start") {
            // FIXME: This is a workaround, and we should find a more global way to solve the problem
            style { focusTraversable = false }
        }
    }
}