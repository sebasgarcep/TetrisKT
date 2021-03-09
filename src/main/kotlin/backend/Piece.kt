package backend

import javafx.scene.paint.Color

class Piece(private val pieceType: PieceType, init: Boolean = false) {
    val blockGrid = BlockGrid(gridSize, gridSize)
    init { if (init) pieceType.initBlockGrid(this) }

    fun setActive(x: Int, y: Int, active: Boolean) = blockGrid.set(x, y, if (active) pieceType.color() else null)
    fun getActive(x: Int, y: Int): Boolean = blockGrid.getActive(x, y)

    fun getColor(): Color = pieceType.color()

    fun getRotate(): Piece {
        val nextPiece = Piece(pieceType)
        for(x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                val centeredX = x - originX
                val centeredY = y - originY
                val nextX = -centeredY + originX
                val nextY = centeredX + originY
                nextPiece.setActive(nextX, nextY, this.getActive(x, y))
            }
        }
        return nextPiece
    }

    companion object {
        const val gridSize = 5
        const val originX = 2
        const val originY = 2

        fun getRandom(): Piece = Piece(PieceType.getRandom(), true)
    }
}