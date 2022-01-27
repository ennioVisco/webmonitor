package at.ac.tuwien.trustcps.reporter

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
import javafx.scene.paint.LinearGradient
import javafx.stage.Stage

/**
 * Plotting class based on JavaFX and Han Solo's Charts library.
 * Instead of being run in classical JavaFX environment, it is being
 * executed as a Runnable
 */
class Plotter(private val title: String, private val data: Array<DoubleArray>)
    : Application(), Runnable
{
    private val rows = data.size
    private val columns = data[0].size
    private var series: MatrixItemSeries<MatrixChartItem>? = null
    private var heatMap: MatrixPane<MatrixChartItem>? = null

    init {
        val color: LinearGradient = Helper.createColorVariationGradient(Color.BLUE, 5)
        val data: MutableList<MatrixChartItem> = ArrayList()
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                data.add(MatrixChartItem(x, y, this.data[y][x]))
            }
        }
        series = MatrixItemSeries(data, ChartType.MATRIX_HEATMAP)
        heatMap = MatrixPane(series)
        //matrixHeatMap2.setColorMapping(ColorMapping.BLUE_TRANSPARENT_RED);
        heatMap!!.matrixGradient = color
        heatMap!!.matrix.setUseSpacer(true)
        heatMap!!.matrix.setColsAndRows(columns, rows)
        heatMap!!.setPrefSize(800.0, 600.0)
    }

    /**
     * Method to run when executing as a Runnable
     */
    override fun run() {
        val stage = Stage()
        val pane = VBox(10.0, heatMap)
        pane.padding = Insets(10.0)
        val scene = Scene(pane)
        stage.title = title
        stage.scene = scene
        stage.show()
    }

    /**
     * Default JavaFX initialization methods. Not supported.
     */
    override fun init() {
        unsupported()
    }

    /**
     * Default JavaFX initialization methods. Not supported.
     */
    override fun start(stage: Stage) {
        unsupported()
    }

    /**
     * Default JavaFX initialization methods. Not supported.
     */
    override fun stop() {
        unsupported()
    }

    private fun unsupported() {
        throw UnsupportedOperationException(
            "Classical execution is not supported. Run the class as a Runnable!")
    }
}