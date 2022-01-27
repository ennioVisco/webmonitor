package at.ac.tuwien.trustcps.checker

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.formula.Formula
import eu.quanticol.moonlight.formula.NegationFormula
import eu.quanticol.moonlight.formula.OrFormula
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

fun impliesFormula(left: Formula, right: Formula): Formula {
    return OrFormula(NegationFormula(left), right)
}
