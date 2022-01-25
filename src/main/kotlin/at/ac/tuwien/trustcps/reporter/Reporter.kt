package at.ac.tuwien.trustcps.reporter

import at.ac.tuwien.trustcps.Plotter
import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import javafx.application.Platform

class Reporter(val rows: Int, val columns: Int) {
    fun <T> report(result: SpatialTemporalSignal<T>, title: String) {
        val monitorValuesB: List<Array<DoubleArray>> = result.signals.map {it.arrayOf(::doubleOf)}
        // Print results
        println(title)
        printResults(monitorValuesB)
    }

    private fun printResults(monitorValues: List<Array<DoubleArray>>) {
        for(l in monitorValues.indices) {
            for (i in monitorValues[l].indices) {
                for (j in monitorValues[l][i].indices) {
                    print(monitorValues[l][i][j])
                    print(" ")
                }
                println("")
            }
        }
    }

    inline fun <reified T> plot(result: SpatialTemporalSignal<T>, title: String)
    {
        val gridValues = signalToGrid(result)
        val plotter = Plotter(title, gridValues)

        Platform.startup {
            plotter.run()
        }
    }

    inline fun <reified T> signalToGrid(signal: SpatialTemporalSignal<T>)
    : Array<DoubleArray>
    {
        val grid = Grid(rows, columns)
        val output: Array<DoubleArray> = Array(grid.rows) {
            DoubleArray(grid.columns) { 0.0 }
        }
        val values = signal.signals.map{
            when(val v = it.valueAt(0.0)) {
                is Boolean -> if (v) 1 else 0
                else -> throw UnsupportedOperationException("Signal type is not supported")
            }
        }
        for(v in values) {
            val xy = grid.toXY(v)
            output[xy.first][xy.second] = v.toDouble()
        }
        return output
    }

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