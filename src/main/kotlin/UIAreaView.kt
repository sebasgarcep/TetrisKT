import tornadofx.*

class UIAreaView : Fragment() {
    override val root = vbox {
        isFillWidth = true
        style { padding = box(PADDING.px) }

        label("Level")
        label("Score")
        button("Start")
    }
}