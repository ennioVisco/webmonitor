package at.ac.tuwien.trustcps

import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import java.util.function.BiFunction

fun evenLocationsAreTrueSignal(size: Int) =
    createStubSignal(size) { _, location ->
        location % 2 == 0
    }

private fun <T> createStubSignal(locations: Int,
                                 f: BiFunction<Double, Int, T>
)
        : SpatialTemporalSignal<T> {
    val s = SpatialTemporalSignal<T>(locations)
    for(time in 0 .. 1) {
        val dTime = time.toDouble()
        s.add(dTime) { f.apply(dTime, it) }
    }
    return s
}