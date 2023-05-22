package com.enniovisco

import eu.quanticol.moonlight.offline.signal.*
import java.util.function.*

val alwaysTrueSignal = run {
    val signal = SpatialTemporalSignal<List<Boolean>>(9)
    signal.add(0.0) { listOf(true) }
    signal.add(1.0) { listOf(true) }
    signal
}

val alwaysFalseSignal = run {
    val signal = SpatialTemporalSignal<List<Boolean>>(9)
    signal.add(0.0) { listOf(false) }
    signal.add(1.0) { listOf(false) }
    signal
}

val alwaysTrueThenAlwaysFalse = run {
    val signal = SpatialTemporalSignal<List<Boolean>>(9)
    signal.add(0.0) { listOf(true) }
    signal.add(1.0) { listOf(false) }
    signal
}

fun evenLocationsAreTrueSignal(size: Int) =
    createStubSignal(size) { _, location ->
        location % 2 == 0
    }

private fun <T> createStubSignal(
    locations: Int,
    f: BiFunction<Double, Int, T>
)
        : SpatialTemporalSignal<T> {
    val s = SpatialTemporalSignal<T>(locations)
    for (time in 0..1) {
        val dTime = time.toDouble()
        s.add(dTime) { f.apply(dTime, it) }
    }
    return s
}
