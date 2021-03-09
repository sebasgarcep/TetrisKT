package backend

import BOARD_HEIGHT
import BOARD_WIDTH

class GameManager {
    var board = BlockGrid(BOARD_WIDTH.toInt(), BOARD_HEIGHT.toInt())
    lateinit var currentPiece: Piece
    var currentX: Int = 0
    var currentY: Int = 0
    lateinit var nextPiece: Piece
    init { setNextPieceState(Piece.getRandom()) }
    var score: Int = 0
    private var rowsCleared: Int = 0

    fun getLevel(): Int = Math.min(20, 1 + (rowsCleared / 10))

    fun tryRotateCurrentPiece() {
        val proposedPiece = currentPiece.getRotate()
        if (testProposedPieceState(proposedPiece, currentX, currentY)) setCurrentPieceState(proposedPiece, currentX, currentY)
    }

    fun tryMoveLeftCurrentPiece() {
        val proposedX = currentX - 1
        if (testProposedPieceState(currentPiece, proposedX, currentY)) setCurrentPieceState(currentPiece, proposedX, currentY)
    }

    fun tryMoveRightCurrentPiece() {
        val proposedX = currentX + 1
        if (testProposedPieceState(currentPiece, proposedX, currentY)) setCurrentPieceState(currentPiece, proposedX, currentY)
    }

    fun tryMoveDownCurrentPiece() {
        val proposedY = currentY + 1
        if (testProposedPieceState(currentPiece, currentX, proposedY)) {
            setCurrentPieceState(currentPiece, currentX, proposedY)
        } else {
            switchToNextPiece()
        }
    }

    private fun switchToNextPiece() {
        // Add block to board
        for (x in 0 until Piece.gridSize) {
            for (y in 0 until Piece.gridSize) {
                // If this is an empty square, then we do not care about it
                if (!currentPiece.getActive(x, y)) continue
                // Define x, y on the board
                val boardX = x + currentX
                val boardY = y + currentY
                // Set color on the board
                board.set(boardX, boardY, currentPiece.getColor())
            }
        }
        // Clear completed lines
        var currentRowsCleared = 0
        for (y in 0 until BOARD_HEIGHT.toInt()) {
            val shouldRemoveLine = (0 until BOARD_WIDTH.toInt()).all { x -> board.getActive(x, y) }
            if (shouldRemoveLine) {
                currentRowsCleared += 1
                // Move all lines above one step down
                for (y2 in (y - 1) downTo 1) {
                    for (x in 0 until BOARD_WIDTH.toInt()) {
                        val color = board.get(x, y2)
                        board.set(x, y2 + 1, color)
                    }
                }
            }
        }
        rowsCleared += currentRowsCleared
        when (currentRowsCleared) {
            1 -> {
                score += 40
            }
            2 -> {
                score += 100
            }
            3 -> {
                score += 300
            }
            4 -> {
                score += 1200
            }
        }
        // Get next piece
        setNextPieceState(nextPiece)
    }

    private fun setNextPieceState(piece: Piece) {
        currentPiece = piece
        currentX = getInitialX()
        currentY = getInitialY()
        nextPiece = Piece.getRandom()
    }

    private fun getInitialX(): Int {
        return (BOARD_WIDTH / 2).toInt() - Piece.originX
    }

    private fun getInitialY(): Int {
        return -Piece.gridSize
    }

    private fun setCurrentPieceState(piece: Piece, x: Int, y: Int) {
        currentPiece = piece
        currentX = x
        currentY = y
    }

    private fun testProposedPieceState(proposedPiece: Piece, proposedX: Int, proposedY: Int): Boolean {
        for (x in 0 until Piece.gridSize) {
            for (y in 0 until Piece.gridSize) {
                // If this is an empty square, then we do not care about it
                if (!proposedPiece.getActive(x, y)) continue
                // Define x, y on the board
                val boardX = x + proposedX
                val boardY = y + proposedY
                // Check lower, left and right bounds
                if (boardX < 0 || boardX >= BOARD_WIDTH || boardY >= BOARD_HEIGHT) return false
                // Check if there is a collision with the board or we are above the board
                if (boardY >= 0 && board.getActive(boardX, boardY)) return false
            }
        }
        return true
    }

    fun getUIBlockGrid(): BlockGrid {
        val blockGrid = BlockGrid(BOARD_WIDTH.toInt(), BOARD_HEIGHT.toInt())
        for (x in 0 until BOARD_WIDTH.toInt()) {
            for (y in 0 until BOARD_HEIGHT.toInt()) {
                val color = board.get(x, y)
                if (color == null) continue
                blockGrid.set(x, y, color)
            }
        }
        val color = currentPiece.getColor()
        for (x in 0 until Piece.gridSize) {
            for (y in 0 until Piece.gridSize) {
                val boardX = x + currentX
                val boardY = y + currentY
                if (boardX < 0 || boardX >= BOARD_WIDTH || boardY < 0 || boardY >= BOARD_HEIGHT) continue
                val active = currentPiece.getActive(x, y)
                if (!active) continue
                blockGrid.set(boardX, boardY, color)
            }
        }
        return blockGrid
    }

    fun tick() {
        tryMoveDownCurrentPiece()
    }
}