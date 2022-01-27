package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.GridSignal
import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import javafx.application.Platform
import java.util.*

/**
 *  Class to handle the reporting of the output
 */
class Reporter(private val grid: Grid) {
    /**
     * Function to record a string with current time
     */
    fun mark(text: String) {
        println("${text}...${Calendar.getInstance().time}")
    }

    /**
     * Function to plot the provided signal
     * @param result signal to be plotted
     * @param title title of the plot window
     */
    fun plot(result: GridSignal, title: String) {
        val gridValues = signalToGrid(result)

        val plotter = Plotter(title, gridValues)

        Platform.startup {
            plotter.run()
        }
    }

    private fun <T> signalToGrid(signal: SpatialTemporalSignal<T>):
            Array<DoubleArray>
    {
        val values = signal.signals.map{ doubleOf(it.valueAt(0.0)) }
        return arrayToMatrix(values)
    }

    private fun arrayToMatrix(values: List<Double>): Array<DoubleArray> {
        val output = Array(grid.columns) { DoubleArray(grid.rows) { 0.0 } }

        for((index, value) in values.withIndex()) {
            val (x, y) = grid.toXY(index)
            output[x][y] = value
        }
        return output
    }

    fun <T> report(result: SpatialTemporalSignal<T>, title: String) {
        val monitorValuesB = result.signals.map { it.arrayOf(::doubleOf) }

        println(title)
        printSTSignal(monitorValuesB)
    }

    /**
     * Prints a sequence of values at time 0.
     * The sequence is indexed on location ids
     * Structure:
     * `values[l][t][0/1]`, where
     * - l location index
     * - t time-point index
     * - 0/1 denoting the exact time of the time-point (0) or its value (1)
     */
    private fun printSTSignal(values: List<Array<DoubleArray>>) {
        for(l in values.indices) {
            println("${values[l][0][1]} ")
        }
    }

    /**
     * Prints a sequence of values for all the time points of the signal.
     * The sequence is indexed on time-point ids
     * Structure:
     * `values[t][0/1]`, where
     * - t time-point index
     * - 0/1 denoting the exact time of the time-point (0) or its value (1)
     */
    private fun printTSignal(values: Array<DoubleArray>) {
        for (i in values.indices) {
            println("${values[i][1]} ")
        }
    }

    fun <T> report(result: Signal<T>, title: String) {
        val monitorValuesB = result.arrayOf(::doubleOf)

        println(title)
        printTSignal(monitorValuesB)
    }

    /**
     * Converts a value of some type to a Double
     * @param T value to convert to Double
     * @throws invalidType when the provided type cannot be handled
     */
    private fun <T> doubleOf(aValue: T): Double {
        return when (aValue) {
            is Double -> aValue
            is Boolean -> if (aValue) 1.0 else -1.0
            else -> invalidType()
        }
    }

    /**
     * Default exception to throw when the type cannot be handled
     */
    private fun invalidType(): Double {
        throw IllegalArgumentException("The provided type is not supported")
    }
}