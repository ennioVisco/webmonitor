package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.space.Grid
import eu.hansolo.fx.charts.ChartType
import eu.hansolo.fx.charts.MatrixPane
import eu.hansolo.fx.charts.data.MatrixChartItem
import eu.hansolo.fx.charts.series.MatrixItemSeries
import eu.hansolo.fx.charts.tools.Helper
import javafx.application.Application
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Insets
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.File
import javax.imageio.ImageIO

/**
 * Plotting class based on JavaFX and Han Solo's Charts library.
 * Instead of being run in classical JavaFX environment, it is being
 * executed as a Runnable
 */
class Plotter(
    private val title: String,
    data: Array<DoubleArray>,
    private val grid: Grid
) : Application(), Runnable {
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
        val color = Color(1.0, 1.0, 1.0, 0.4)
        return MatrixPane(color, series)
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

    private fun spawnStage(): Stage = Stage()

    private fun configStage(stage: Stage) {
        val pane = StackPane(heatMap)
        pane.padding = Insets(6.0, 0.0, 0.0, 0.0)
        val scene = Scene(pane)

        pane.background = setBackground("image.png")
//        pane.width = grid.columns.toDouble()
//        pane.height = grid.rows.toDouble()

        println("Panel size ${pane.width}x${pane.height}")
        stage.title = title
        stage.scene = scene
        takeSnapshot(scene, "image2.png")
    }

    private fun takeSnapshot(scene: Scene, fileName: String) {
        val image = scene.snapshot(null)
        val file = File("./$fileName")
        val buffer = SwingFXUtils.fromFXImage(image, null)
        ImageIO.write(buffer, "PNG", file)
    }

    private fun setBackground(fileName: String): Background {
        val backgroundURL = File(fileName).canonicalFile.toURI().toURL().toString()
        val image = Image(backgroundURL)
        val position = BackgroundPosition(
            Side.LEFT, 0.0, true,
            Side.TOP, -6.0, false
        )
        val backgroundImage = BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT
            //BackgroundSize(500.0, 860.0, false, false, false, false)
        )
        return Background(backgroundImage)
    }

    private fun configPlot() {
        //matrixHeatMap2.setColorMapping(ColorMapping.BLUE_TRANSPARENT_RED)
        val color = Helper.createColorVariationGradient(Color.BLUE, 5)
        heatMap.matrixGradient = color
        heatMap.matrix.setUseSpacer(false)
        heatMap.matrix.setColsAndRows(columns, rows)
        heatMap.setPrefSize(grid.columns.toDouble(), grid.rows.toDouble())
    }

    private fun showStage(stage: Stage) = stage.show()

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
            "Classical execution is not supported. Run the class as a Runnable!"
        )
    }
}