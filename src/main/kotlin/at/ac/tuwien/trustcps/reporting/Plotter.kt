package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.space.Grid
import eu.hansolo.fx.charts.ChartType
import eu.hansolo.fx.charts.MatrixPane
import eu.hansolo.fx.charts.data.MatrixChartItem
import eu.hansolo.fx.charts.series.MatrixItemSeries
import javafx.application.Application
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.transform.Scale
import javafx.stage.Stage
import java.io.File
import javax.imageio.ImageIO

/**
 * Plotting class based on JavaFX and Han Solo's Charts library.
 * Instead of being run in classical JavaFX environment, it is being
 * executed as a Runnable
 * TODO: Warning - the desktop environment must be able to
 *                 open windows of the submitted size
 */
class Plotter(
    private val id: Int,
    private val title: String,
    data: Array<DoubleArray>,
    private val grid: Grid,
    private val withBackground: Boolean = false
) : Application(), Runnable {
    private val columns = data.size //TODO: should compare to grid
    private val rows = data[0].size
    private val heatMap = addData(data)

    companion object {
        var running = false
    }

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
        //pane.padding = Insets(6.0, 0.0, 0.0, 0.0)
        val scene = Scene(pane)
        if (withBackground) {
            pane.background = setBackground("output/snap_${id}.png")
        }
        stage.title = title
        stage.scene = scene
        takeSnapshot(scene, "output/eval_${id}.png")
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
        val backgroundImage = BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT
        )
        return Background(backgroundImage)
    }

    private fun configPlot() {
        //matrixHeatMap2.setColorMapping(ColorMapping.BLUE_TRANSPARENT_RED)
        //val color = Helper.createColorVariationGradient(Color.BLUE, 5)
        heatMap.matrixGradient = setColorGradient()
        heatMap.matrix.setUseSpacer(false)
        heatMap.matrix.setColsAndRows(columns, rows)
        heatMap.setPrefSize(grid.columns.toDouble(), grid.rows.toDouble())
        //heatMap.transforms.add(rescale())
    }

    private fun rescale(): Scale {
        val scale = Scale()
        //scale.x = 0.8
        //scale.y = 0.8
        return scale
    }

    private fun setColorGradient(): LinearGradient {
        val red = Color.color(0.831, 0.275, 0.275, 0.5)
        val green = Color.color(0.0, 0.831, 0.275, 0.5)
        val stops = listOf(Stop(0.0, red), Stop(1.0, green))
        return LinearGradient(
            0.0, 0.0, 1.0, 0.0,
            true, CycleMethod.NO_CYCLE, stops
        )
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