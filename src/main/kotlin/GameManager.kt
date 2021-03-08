import javafx.scene.paint.Color

class BlockGrid(val width: Int, val height: Int) {
    private val grid = Array(width) { Array<Color?>(height) { null } }
    fun get(x: Int, y: Int): Color? = grid[x][y]
    fun getActive(x: Int, y: Int): Boolean = get(x, y) != null
    fun set(x: Int, y: Int, value: Color?) { grid[x][y] = value }
}

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

class Piece(private val pieceType: PieceType, init: Boolean = false) {
    private val blockGrid = BlockGrid(gridSize, gridSize)
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

class GameManager {
    var board = BlockGrid(BOARD_WIDTH.toInt(), BOARD_HEIGHT.toInt())
    lateinit var currentPiece: Piece
    var currentX: Int = 0
    var currentY: Int = 0
    lateinit var nextPiece: Piece
    init { setNextPieceState(Piece.getRandom()) }

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
        for (y in 0 until BOARD_HEIGHT.toInt()) {
            val shouldRemoveLine = (0 until BOARD_WIDTH.toInt()).all { x -> board.getActive(x, y) }
            if (shouldRemoveLine) {
                // Move all lines above one step down
                for (y2 in (y - 1) downTo 1) {
                    for (x in 0 until BOARD_WIDTH.toInt()) {
                        val color = board.get(x, y2)
                        board.set(x, y2 + 1, color)
                    }
                }
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

    fun tick(): BlockGrid {
        tryMoveDownCurrentPiece()
        return getUIBlockGrid()
    }
}