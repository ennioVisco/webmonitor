package com.enniovisco.reporting

import com.enniovisco.*
import com.enniovisco.space.*
import io.github.moonlightsuite.moonlight.offline.signal.*
import javafx.application.*
import java.io.*
import java.time.*
import java.time.temporal.*


/**
 *  Class to handle the reporting of the output
 */
class Reporter(
    private val toConsole: Boolean = false,
    private val toFile: Boolean = false,
    var devicePixelRatio: Double = 1.0,
    private val logTimeGranularity: TemporalUnit = ChronoUnit.SECONDS,
) : AutoCloseable {
    private val log = io.github.oshai.kotlinlogging.KotlinLogging.logger {}
    private val headless: Boolean

    init {
        val isHeadless = System.getProperty("testfx.headless")
        headless = isHeadless != null && isHeadless == "true"
    }

    private fun logTime() = LocalDateTime.now().truncatedTo(logTimeGranularity)

    private val buffer = mutableListOf<String>()

    private fun outputLine(text: String) = "[${logTime()}] - $text\n"

    fun mark(text: String, important: Boolean = false) {
        val data = outputLine(text)
        if (toConsole || important) {
            log.info { "==== $text ====" }
        }
        buffer.add(data)
    }

    fun title(text: String) {
        mark(text, true)
    }

    /**
     * Function to record a string with current time
     */
    private fun sendOutput(text: String, out: PrintWriter) {
        val data = outputLine(text)
        out.write(data)
    }

    /**
     * Function to plot the provided signal
     * @param result signal to be plotted
     * @param title title of the plot window
     */
    fun plot(id: Int, result: GridSignal, grid: Grid, title: String) {
        val gridValues = signalToGrid(result, grid)

        spawnPlotter(id, title, gridValues, grid)
    }

    private fun spawnPlotter(
        id: Int,
        title: String,
        values: Array<DoubleArray>,
        grid: Grid
    ) {
        try {
            Platform.startup(doPlot(id, title, values, grid))
        } catch (e: IllegalStateException) {
            log.info { "JavaFX platform already instantiated. Skipping." }
            Platform.runLater(doPlot(id, title, values, grid))
        }
    }

    private fun doPlot(
        id: Int,
        title: String,
        values: Array<DoubleArray>,
        grid: Grid
    ) = Plotter(id, title, values, grid, devicePixelRatio, headless = headless)

    private fun <T> signalToGrid(signal: SpatialTemporalSignal<T>, grid: Grid) =
        arrayToMatrix(signal.signals.map {
            doubleOf(it.getValueAt(0.0))
        }, grid)


    private fun arrayToMatrix(values: List<Double>, grid: Grid):
            Array<DoubleArray> {
        val output = Array(grid.columns) { DoubleArray(grid.rows) { 0.0 } }

        for ((index, value) in values.withIndex()) {
            val (x, y) = grid.toXY(index)
            output[x][y] = value
        }
        return output
    }

    fun <T> report(result: SpatialTemporalSignal<T>, title: String) {
        val monitorValuesB = result.signals.map { it.arrayOf(::doubleOf) }

        generateOutput("st_dump") {
            sendOutput(title, it)
            printSTSignal(monitorValuesB, it)
        }
    }

    private fun generateOutput(
        name: String,
        block: (output: PrintWriter) -> Unit
    ) {
        val now = logTimeGranularity.toString()
            .replace("-", "")
            .replace(":", "")

        val pathName = "output/${name}_${now}.txt"

        if (toFile) {
            File(pathName).printWriter().use {
                buffer.forEach { line -> sendOutput(line, it) }
                block(it)
            }
        }

        if (toConsole) {
            PrintWriter(System.out).use {
                buffer.forEach { line -> sendOutput(line, it) }
                block(it)
            }
        }
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
    private fun printSTSignal(
        values: List<Array<DoubleArray>>,
        outputFile: PrintWriter
    ) {
        for (l in values.indices) {
            sendOutput("${values[l][0][1]}", outputFile)
        }
    }

    /**
     * Prints a sequence of values for all the time points of the signal.
     * The sequence is indexed on time-point ids
     * Structure: `values[t][0/1]`, where
     * - t time-point index
     * - 0/1 denoting the exact time of the time-point (0) or its value (1)
     */
    private fun printTSignal(
        values: Array<DoubleArray>,
        outputFile: PrintWriter
    ) {
        for (i in values.indices) {
            sendOutput("${values[i][1]}", outputFile)
        }
    }

    fun <T> report(result: Signal<T>, title: String) {
        val monitorValuesB = result.arrayOf(::doubleOf)
        generateOutput("t_output") {
            sendOutput(title, it)
            printTSignal(monitorValuesB, it)
        }
    }

    /**
     * Converts a value of some type to a Double
     * @param T value to convert to Double
     * @throws invalidType when the provided type cannot be handled
     */
    private fun <T> doubleOf(aValue: T) = when (aValue) {
        is Double -> aValue
        is Boolean -> if (aValue) 1.0 else -1.0
        else -> if (aValue !== null) {
            invalidType(aValue)
        } else -1.0
    }

    /**
     * Default exception to throw when the type cannot be handled
     */
    private fun invalidType(value: Any): Double {
        throw IllegalArgumentException("The provided type is not supported: $value")
    }

    override fun close() {
        buffer.clear()
    }
}
