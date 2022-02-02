package at.ac.tuwien.trustcps.reporting

import eu.hansolo.fx.charts.ChartType
import eu.hansolo.fx.charts.MatrixPane
import eu.hansolo.fx.charts.data.MatrixChartItem
import eu.hansolo.fx.charts.series.MatrixItemSeries
import eu.hansolo.fx.charts.tools.Helper
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

/**
 * Plotting class based on JavaFX and Han Solo's Charts library.
 * Instead of being run in classical JavaFX environment, it is being
 * executed as a Runnable
 */
class Plotter(private val title: String, data: Array<DoubleArray>)
    : Application(), Runnable {
    private val columns = data.size
    private val rows = data[0].size
    private val heatMap = addData(data)

    /**
     * Initializes the data to plot a heatmap
     */
    private fun addData(data: Array<DoubleArray>): MatrixPane<MatrixChartItem> {
        val plotData: MutableList<MatrixChartItem> = ArrayList()
        for (x in 0 until columns) {
            for (y in 0 until rows) {
                plotData.add(MatrixChartItem(x, y, data[x][y]))
            }
        }
        val series = MatrixItemSeries(plotData, ChartType.MATRIX_HEATMAP)
        return MatrixPane(series)
    }

    /**
     * Method to run when executing as a Runnable
     */
    override fun run() {
        configPlot()
        val stage = spawnStage()
        configStage(stage)
        showStage(stage)
    }

    private fun spawnStage(): Stage  = Stage()

    private fun configStage(stage: Stage) {
        val pane = VBox(10.0, heatMap)
        pane.padding = Insets(10.0)
        val scene = Scene(pane)
        stage.title = title
        stage.scene = scene
    }

    private fun configPlot() {
        //matrixHeatMap2.setColorMapping(ColorMapping.BLUE_TRANSPARENT_RED)
        val color = Helper.createColorVariationGradient(Color.BLUE, 5)
        heatMap.matrixGradient = color
        heatMap.matrix.setUseSpacer(true)
        heatMap.matrix.setColsAndRows(columns, rows)
        heatMap.setPrefSize(900.0, 900.0)
    }

    private fun showStage(stage: Stage)  = stage.show()

    /**
     * Default JavaFX initialization methods. Not supported.
     */
    override fun init() = unsupported()

    /**
     * Default JavaFX starting methods. Not supported.
     */
    override fun start(stage: Stage) = unsupported()

    /**
     * Default JavaFX stopping methods. Not supported.
     */
    override fun stop() = unsupported()

    private fun unsupported() {
        throw UnsupportedOperationException(
            "Classical execution is not supported. Run the class as a Runnable!")
    }
}