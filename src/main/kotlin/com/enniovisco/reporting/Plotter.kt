package com.enniovisco.reporting

import com.enniovisco.space.Grid
import eu.hansolo.fx.charts.*
import eu.hansolo.fx.charts.data.*
import eu.hansolo.fx.charts.series.*
import javafx.application.*
import javafx.embed.swing.*
import javafx.scene.*
import javafx.scene.image.*
import javafx.scene.layout.*
import javafx.scene.paint.*
import javafx.stage.*
import java.io.*
import javax.imageio.*
import kotlin.math.*

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
    private val deviceRatio: Double,
    private val withBackground: Boolean = true,
    private val headless: Boolean = false
) : Application(), Runnable {
    private val columns = data.size
    private val rows = data[0].size
    private val heatMap = addData(data)
    private val log = io.github.oshai.kotlinlogging.KotlinLogging.logger {}

    init {
        if (grid.columns != columns || grid.rows != rows)
            throw IllegalArgumentException("Grid and data dimensions do not match")
    }

    /**
     * Initializes the data to plot a heatmap
     */
    private fun addData(data: Array<DoubleArray>): MatrixPane<MatrixChartItem> {
        val plotData = (0 until columns).map { x ->
            (0 until rows).map { y ->
                MatrixChartItem(x, y, data[x][y])
            }
        }.flatten()
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
        val scene = Scene(pane)
        if (withBackground) {
            pane.background = setBackground("output/snap_${id}.png")
        }
        stage.title = title
        stage.scene = scene
        Platform.runLater {
            takeSnapshot(scene, "output/eval_${id}.png")
            if (headless) {
                log.info { "Headless mode, closing GUI." }
                Platform.exit()
            }
        }
    }

    private fun takeSnapshot(scene: Scene, fileName: String) {
        log.info { "Taking snapshot of $fileName" }
        val image = scene.snapshot(null)
        val file = File("./$fileName")
        val buffer = SwingFXUtils.fromFXImage(image, null)
        ImageIO.write(buffer, "PNG", file)
    }

    private fun setBackground(fileName: String): Background {
        val backgroundURL =
            File(fileName).canonicalFile.toURI().toURL().toString()
        val image = Image(backgroundURL)

        val width = (image.width / deviceRatio).roundToInt().toDouble()
        val height = (image.height / deviceRatio).roundToInt().toDouble()

        val backgroundSize = BackgroundSize(
            width,
            height,
            false,
            false,
            false,
            false
        )

        val backgroundImage = BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            backgroundSize
        )
        return Background(backgroundImage)
    }

    private fun configPlot() {
        heatMap.matrixGradient = setColorGradient()
        heatMap.matrix.setUseSpacer(false)
        heatMap.matrix.setColsAndRows(columns, rows)
        heatMap.setPrefSize(grid.columns.toDouble(), grid.rows.toDouble())
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
