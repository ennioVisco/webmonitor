package at.ac.tuwien.trustcps.checker

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

class Checker(width: Int, height: Int,
              data: Map<String, String>
)
{
    val grid = Grid(width, height)
    val locationService = StaticLocationService(grid.model)
    val signal = SignalBuilder(grid, 1, data).signal //TODO: change signal

    private val atoms = HashMap<String,
            Function<Parameters?,
                    Function<Pair<Boolean, Boolean>, Boolean>>>()

    private val dist = mutableMapOf<String, Function<SpatialModel<Int>, DistanceStructure<Int, *>>>(Pair("base", Function {  grid.distance() }))


    init {
        setAtomicFormulas()
        //val dist = mutableMapOf<String, Function<SpatialModel<Int>, DistanceStructure<Int, Int>>>(Pair("base", Function {  grid.distance2() }))
    }

    fun check(spec: Formula): SpatialTemporalSignal<Boolean> {
        val monitor = SpatialTemporalMonitoring(atoms, dist, BooleanDomain(), true)
        return monitor.monitor(spec, null).monitor(locationService, signal)
    }

    private fun setAtomicFormulas() {
        atoms["#cookieman-modal p"] = Function {
            Function { pair: Pair<Boolean, Boolean> -> pair.second }
        }
        atoms["screen"] = Function {
            Function { pair: Pair<Boolean, Boolean> -> pair.first }
        }

    }

}