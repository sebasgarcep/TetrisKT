import backend.BlockGrid
import javafx.scene.canvas.GraphicsContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun setInterval(ms: Int, callback: () -> Unit): Job {
    val msLong = ms.toLong()
    return GlobalScope.launch {
        while (true) {
            delay(msLong)
            callback()
        }
    }
}

fun drawBlockGridOnCanvas(graphicsContext2D: GraphicsContext, blockGrid: BlockGrid) {
    graphicsContext2D.clearRect(0.0, 0.0, blockGrid.width * BLOCK_SIZE, blockGrid.height * BLOCK_SIZE)
    for (x in 1 until blockGrid.width) {
        graphicsContext2D.strokeLine(x.toDouble() * BLOCK_SIZE,0.0, x.toDouble() * BLOCK_SIZE, blockGrid.height * BLOCK_SIZE)
    }
    for (y in 1 until blockGrid.height) {
        graphicsContext2D.strokeLine(0.0, y.toDouble() * BLOCK_SIZE, blockGrid.width * BLOCK_SIZE, y.toDouble() * BLOCK_SIZE)
    }
    for (x in 0 until blockGrid.width) {
        for (y in 0 until blockGrid.height) {
            val color = blockGrid.get(x, y)
            if (color == null) continue
            graphicsContext2D.fill = color
            graphicsContext2D.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
        }
    }
}