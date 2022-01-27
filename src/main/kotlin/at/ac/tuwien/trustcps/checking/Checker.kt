package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.ELEMENT
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


class Checker(private val grid: Grid, data: List<Map<String, String>>)
{
    private val element = ELEMENT
    private val locationService = StaticLocationService(grid.model)
    val signal = SignalBuilder(grid, listOf(element), data).signal

    private val atoms = mapOf<String,
                              Function<Parameters?,
                                    Function<Pair<Boolean, Boolean>, Boolean>>>(
        element to Function { Function { (_, s) -> s } },
        "screen" to Function { Function { (f, _) -> f } }
    )

    private val dist = mapOf<String,
                                    Function<SpatialModel<Int>,
                                            DistanceStructure<Int, *>>>(
        Pair("base", Function { grid.distance() })
    )

    fun check(spec: Formula): SpatialTemporalSignal<Boolean> {
        val m = SpatialTemporalMonitoring(atoms, dist, BooleanDomain(), true)
        return m.monitor(spec, null).monitor(locationService, signal)
    }
}
