package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import java.util.stream.IntStream

/**
 *  Builder class that generates a signal based on the data
 *  @param grid
 *  @param props
 *  @param data
 */
class SignalBuilder(grid: Grid,
                    props: List<String>,
                    data: List<Map<String, String>>)
{
    val signal = generateSTSignal(grid, props, data)

    private fun generateSTSignal(grid: Grid,
                                 props: List<String>,
                                 data: List<Map<String, String>>)
    : SpatialTemporalSignal<Pair<Boolean, Boolean>>
    {
//        if(data.isEmpty())
//            throw IllegalArgumentException("Empty data passed to signal builder!")
        val stSignal = SpatialTemporalSignal<Pair<Boolean, Boolean>>(grid.size)
        IntStream.range(0, data.size).forEach { time: Int ->
            val screen = screenToBox(data[time])
            for(p in props) {   //TODO: only works with single element in list

                stSignal.add(time.toDouble()) { location: Int ->
                    val elem = dataToBox(p, data[time])
                    Pair(screen.contains(grid.toXY(location)),
                        elem.contains(grid.toXY(location))
                    )
                }
            }

        }
        return stSignal
    }

    private fun screenToBox(snapshot: Map<String, String>): Box {
        val maxX = snapshot["wnd_width"]!!.toInt()
        val maxY = snapshot["wnd_height"]!!.toInt()
        return Box(0, 0, maxX, maxY)
    }

    private fun dataToBox(id: String, snapshot: Map<String, String>): Box {
        val minX = snapshot["$id::x"]!!.toInt()
        val minY = snapshot["$id::y"]!!.toInt()
        val maxX = minX + snapshot["$id::width"]!!.toInt()
        val maxY = minY + snapshot["$id::height"]!!.toInt()
        return Box(minX, minY, maxX, maxY)
    }

    data class Box(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)
    {
        fun contains(coords: Pair<Int, Int>): Boolean {
            return contains(coords.first, coords.second)
        }

        fun contains(x: Int, y: Int): Boolean {
            return x in minX..maxX
                && y in minY..maxY
        }
    }
}