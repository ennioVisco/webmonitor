package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.formula.BooleanDomain
import eu.quanticol.moonlight.formula.Formula
import eu.quanticol.moonlight.formula.Parameters
import eu.quanticol.moonlight.monitoring.SpatialTemporalMonitoring
import eu.quanticol.moonlight.signal.DistanceStructure
import eu.quanticol.moonlight.signal.SpatialModel
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import eu.quanticol.moonlight.signal.StaticLocationService
import java.util.function.Function

typealias Monitor<V, T, R> = SpatialTemporalMonitoring<V, T, R>
typealias Interpretation<T> = Function<Parameters?, Function<T, Boolean>>

/**
 *
 */
class Checker(private val grid: Grid,
              data: List<Map<String, String>>,
              elements: List<String>) {
    private val locationService = StaticLocationService(grid.model)

    private val signal = TraceBuilder(grid, data).useMetadata()
                                         .useElements(elements)
                                         .build()

    private val dist = mapOf<String, Function<SpatialModel<Int>,
                                              DistanceStructure<Int, *>>>(
        Pair("all", Function { grid.distance() }),
        Pair("base", Function { grid.distance(1) })
    )

    val atoms = elems(elements)

    fun check(spec: Formula): SpatialTemporalSignal<Boolean> {
        val m = Monitor(atoms, dist, BooleanDomain(), true)
        return m.monitor(spec, null).monitor(locationService, signal)
    }

    private fun elems(elems: List<String>):
            Map<String, Interpretation<List<Boolean>>> {
        val atoms = mutableMapOf<String, Interpretation<List<Boolean>>>(
            "screen" to Function { Function { it[0] } }
        )
        for(i in elems.indices) {
            atoms[elems[i]] = Function { Function { it[i + 1] } }
        }
        return atoms
    }


}
