package at.ac.tuwien.trustcps.reporter

import eu.quanticol.moonlight.signal.Signal

class Reporter {
    fun <T> report(result: Signal<T>, title: String) {
        val monitorValuesB: Array<DoubleArray> = result.arrayOf(::doubleOf)
        // Print results
        println(title)
        printResults(monitorValuesB)
    }

    private fun printResults(monitorValues: Array<DoubleArray>) {
        for (i in monitorValues.indices) {
            for (j in monitorValues[i].indices) {
                print(monitorValues[i][j])
                print(" ")
            }
            println("")
        }
    }

    private fun <T> doubleOf(aValue: T): Double {
        return when (aValue) {
            is Double -> aValue
            is Boolean -> if (aValue) 1.0 else -1.0
            else -> Double.NaN
        }
    }
}