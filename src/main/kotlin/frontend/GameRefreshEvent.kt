package frontend

import backend.BlockGrid
import tornadofx.FXEvent

class GameRefreshEvent(val blockGrid: BlockGrid, val level: Int, val score: Int) : FXEvent()
