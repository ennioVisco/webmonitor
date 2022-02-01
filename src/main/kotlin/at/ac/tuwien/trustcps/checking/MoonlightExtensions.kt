package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.formula.Formula
import eu.quanticol.moonlight.formula.NegationFormula
import eu.quanticol.moonlight.formula.OrFormula
import eu.quanticol.moonlight.signal.SpatialTemporalSignal

/**
 * Requires a signal based on the provided grid
 * @return a spatial snapshot given a grid on which the signal is based,
 *         and a time point
 */
inline fun <reified T> SpatialTemporalSignal<T>.as2dSnapshot(grid: Grid,
                                                             time: Double)
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

/**
 * Helper method to generate a formula based on an implication
 */
fun impliesFormula(left: Formula, right: Formula): Formula {
    return OrFormula(NegationFormula(left), right)
}
