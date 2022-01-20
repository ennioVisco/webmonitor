package at.ac.tuwien.trustcps.checker

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import java.util.stream.IntStream

class SignalBuilder(grid: Grid, times: Int,
                    private val data: Map<String, String>)
{
    val signal = generateSTSignal(grid, times)

    private fun generateSTSignal(grid: Grid, times: Int): SpatialTemporalSignal<Pair<Boolean, Boolean>>
    {
        val stSignal = SpatialTemporalSignal<Pair<Boolean, Boolean>>(grid.size)
        val elem = dataToBox("#cookieman-modal p")
        val screen = screenToBox()
        IntStream.range(0, 1).forEach { time: Int ->
            stSignal.add(time.toDouble()) { location: Int ->
                Pair(screen.contains(grid.toXY(location)),
                    elem.contains(grid.toXY(location)))
            }
        }
        return stSignal
    }

    private fun screenToBox(): Box {
        val maxX = data["wnd_width"]!!.toInt()
        val maxY = data["wnd_height"]!!.toInt()
        return Box(0, 0, maxX, maxY)
    }

    private fun dataToBox(id: String): Box {
        val minX = data["$id::x"]!!.toInt()
        val minY = data["$id::y"]!!.toInt()
        val maxX = minX + data["$id::width"]!!.toInt()
        val maxY = minY + data["$id::height"]!!.toInt()
        return Box(minX, minY, maxX, maxY)
    }

    data class Box(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int) {

        fun contains(coords: Pair<Int, Int>): Boolean {
            return contains(coords.first, coords.second)
        }

        fun contains(x: Int, y: Int): Boolean {
            return x in minX..maxX &&
                    y in minY..maxY
        }
    }
}