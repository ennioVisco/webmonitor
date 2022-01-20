package at.ac.tuwien.trustcps.checker

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.formula.Parameters
import eu.quanticol.moonlight.signal.StaticLocationService
import eu.quanticol.moonlight.util.Pair
import java.util.function.Function

class Checker(width: Int, height: Int, private val data: Map<String, String>)
{
    val grid = Grid(width, height)
    val locationService = StaticLocationService(grid.model)
    val signal = SignalBuilder(grid, 1, data).signal //TODO: change signal

    val atomicFormulas = HashMap<String,
            Function<Parameters,
                    Function<Pair<Boolean, Boolean>, Boolean>>>()

    init {
        setAtomicFormulas()
    }

    private fun setAtomicFormulas() {
        atomicFormulas["#cookieman-modal p"] = Function {
            Function { pair: Pair<Boolean, Boolean> -> pair.second }
        }
        atomicFormulas["screen"] = Function {
            Function { pair: Pair<Boolean, Boolean> -> pair.first }
        }

    }

}