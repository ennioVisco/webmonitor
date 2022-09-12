package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.*
import at.ac.tuwien.trustcps.space.*
import eu.quanticol.moonlight.offline.signal.*
import javafx.application.*
import java.io.*
import java.time.*
import java.time.temporal.*


/**
 *  Class to handle the reporting of the output
 */
class Reporter(
    private val toConsole: Boolean = false,
    private val toFile: Boolean = false
) {
    private val logTimeGranularity =
        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

    private val buffer = mutableListOf<String>()

    private fun outputLine(text: String) = "[$logTimeGranularity] - $text\n"

    fun mark(text: String) {
        val data = outputLine(text)
        if (toConsole) {
            print(data)
        }
        buffer.add(data)
    }

    fun title(text: String) {
        println(text)
        mark(text)
    }

    /**
     * Function to record a string with current time
     */
    private fun sendOutput(text: String, out: PrintWriter) {
        val data = outputLine(text)
        if (toConsole) {
            print(data)
        }
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
            Platform.startup(Plotter(id, title, values, grid))
        } catch (e: IllegalStateException) {
            println("JavaFX platform already instantiated. Skipping.")
            Platform.runLater(Plotter(id, title, values, grid))
        } finally {
            Platform.exit()
        }
    }

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

        File(pathName).printWriter().use {
            buffer.forEach { line -> sendOutput(line, it) }
            block(it)
        }

        // TODO: Workaround, needs refactoring
        if (!toFile) {
            File(pathName).delete()
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
        else -> invalidType()
    }

    /**
     * Default exception to throw when the type cannot be handled
     */
    private fun invalidType(): Double {
        throw IllegalArgumentException("The provided type is not supported")
    }
}
