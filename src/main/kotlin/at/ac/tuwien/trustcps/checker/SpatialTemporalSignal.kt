package at.ac.tuwien.trustcps.checker

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.SpatialTemporalSignal

inline fun <reified T> SpatialTemporalSignal<T>.get2dSnapshot(grid: Grid, time: Double)
: Array<Array<T>>
{
    val values: List<T> = this.signals.map { it.valueAt(time) }

    val output: Array<Array<T>> = Array(grid.rows) { y ->
        Array(grid.columns) { x ->
            values[grid.toNode(Pair(x, y))]
        }
    }
    return output
}
