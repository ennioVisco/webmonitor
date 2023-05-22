package com.enniovisco.checking

import com.enniovisco.space.*
import eu.quanticol.moonlight.core.formula.*
import eu.quanticol.moonlight.core.space.*
import eu.quanticol.moonlight.domain.*
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.offline.monitoring.*
import eu.quanticol.moonlight.offline.signal.*
import eu.quanticol.moonlight.space.*
import java.util.function.Function

typealias Monitor<V, T, R> = SpatialTemporalMonitoring<V, T, R>
typealias Interpretation<T> = Function<Parameters?, Function<T, Boolean>>

/**
 *
 */
class Checker(
    private val grid: Grid,
    private val data: List<Map<String, String>>,
    elements: Map<String, (String, String) -> Boolean>
) {
    private val locationService = StaticLocationService(grid.model)

    private val signal = TraceBuilder(grid, data)
        .useMetadata()
        .useElements(elements)
        .build()

    private val dist = mapOf<String, Function<SpatialModel<Int>,
            DistanceStructure<Int, *>>>(
        Pair("all", Function { grid.distance() }),
        Pair("base", Function { grid.distance(1) })
    )

    val atoms = elems(elements.map { it.key })

    fun check(spec: Formula): SpatialTemporalSignal<Boolean> {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Empty data passed for the trace.")
        }
        val m = Monitor(atoms, dist, BooleanDomain(), true)
        return m.monitor(spec).monitor(locationService, signal)
    }

    private fun elems(elems: List<String>):
            Map<String, Interpretation<List<Boolean>>> {
        val atoms = mutableMapOf<String, Interpretation<List<Boolean>>>(
            "screen" to Function { Function { it[0] } }
        )
        for (i in elems.indices) {
            atoms[elems[i]] = Function { Function { it[i + 1] } }
        }
        return atoms
    }


}
