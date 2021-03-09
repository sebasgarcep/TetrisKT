package backend

import javafx.scene.paint.Color

class BlockGrid(val width: Int, val height: Int) {
    private val grid = Array(width) { Array<Color?>(height) { null } }
    fun get(x: Int, y: Int): Color? = grid[x][y]
    fun getActive(x: Int, y: Int): Boolean = get(x, y) != null
    fun set(x: Int, y: Int, value: Color?) { grid[x][y] = value }
}