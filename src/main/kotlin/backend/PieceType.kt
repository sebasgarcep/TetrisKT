package backend

import javafx.scene.paint.Color

enum class PieceType {
    O {
        override fun color(): Color = Color.YELLOW
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY + 1, true)
            piece.setActive(Piece.originX + 1, Piece.originY, true)
            piece.setActive(Piece.originX + 1, Piece.originY + 1, true)
        }
    },
    I {
        override fun color(): Color = Color.CYAN
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY - 1, true)
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY + 1, true)
            piece.setActive(Piece.originX, Piece.originY + 2, true)
        }
    },
    T {
        override fun color(): Color = Color.PURPLE
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY - 1, true)
            piece.setActive(Piece.originX - 1, Piece.originY, true)
            piece.setActive(Piece.originX + 1, Piece.originY, true)
        }
    },
    S {
        override fun color(): Color = Color.GREEN
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX + 1, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY + 1, true)
            piece.setActive(Piece.originX - 1, Piece.originY + 1, true)
        }
    },
    Z {
        override fun color(): Color = Color.RED
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX - 1, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY + 1, true)
            piece.setActive(Piece.originX + 1, Piece.originY + 1, true)
        }
    },
    L {
        override fun color(): Color = Color.ORANGE
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY - 1, true)
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY + 1, true)
            piece.setActive(Piece.originX + 1, Piece.originY + 1, true)
        }
    },
    J {
        override fun color(): Color = Color.BLUE
        override fun initBlockGrid(piece: Piece) {
            piece.setActive(Piece.originX, Piece.originY - 1, true)
            piece.setActive(Piece.originX, Piece.originY, true)
            piece.setActive(Piece.originX, Piece.originY + 1, true)
            piece.setActive(Piece.originX - 1, Piece.originY + 1, true)
        }
    };

    abstract fun color(): Color
    abstract fun initBlockGrid(piece: Piece)

    companion object {
        fun getRandom(): PieceType = values().random()
    }
}