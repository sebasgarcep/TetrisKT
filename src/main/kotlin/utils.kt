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