package at.ac.tuwien.trustcps

import eu.hansolo.fx.charts.ChartType
import eu.hansolo.fx.charts.MatrixPane
import eu.hansolo.fx.charts.data.MatrixChartItem
import eu.hansolo.fx.charts.series.MatrixItemSeries
import eu.hansolo.fx.charts.tools.ColorMapping
import eu.hansolo.fx.charts.tools.Helper
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.LinearGradient
import javafx.stage.Stage
import javafx.geometry.Insets
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.exitProcess

class PlotterCopy : Application() {
    private var matrixItemSeries1: MatrixItemSeries<MatrixChartItem?>? = null
    private var matrixHeatMap1: MatrixPane<MatrixChartItem?>? = null
    private var factor = 0.0
    private var matrixItemSeries2: MatrixItemSeries<MatrixChartItem?>? = null
    private var matrixHeatMap2: MatrixPane<MatrixChartItem?>? = null
    private var matrixItemSeries3: MatrixItemSeries<MatrixChartItem>? = null
    private var matrixHeatMap3: MatrixPane<MatrixChartItem>? = null
    private var lastTimerCall: Long = 0
    private var timer: AnimationTimer? = null

    override fun init() {
        matrixInit()
//        lastTimerCall = System.nanoTime()
//        timer = timer()
    }

    private fun matrixInit() {
        val matrixGradient: LinearGradient = Helper.createColorVariationGradient(Color.BLUE, 5)
        val matrixData2: MutableList<MatrixChartItem?> = ArrayList()
        for (y in 0..5) {
            for (x in 0..7) {
                matrixData2.add(MatrixChartItem(x, y, RND.nextDouble()))
            }
        }
        matrixItemSeries2 = MatrixItemSeries<MatrixChartItem?>(matrixData2, ChartType.MATRIX_HEATMAP)
        matrixHeatMap2 = MatrixPane<MatrixChartItem?>(matrixItemSeries2)
        //matrixHeatMap2.setColorMapping(ColorMapping.BLUE_TRANSPARENT_RED);
        matrixHeatMap2!!.matrixGradient = matrixGradient
        matrixHeatMap2!!.matrix.setUseSpacer(true)
        matrixHeatMap2!!.matrix.setColsAndRows(8, 6)
        matrixHeatMap2!!.setPrefSize(400.0, 300.0)
    }

    private fun matrix1Init() {
        matrixItemSeries1 = MatrixItemSeries<MatrixChartItem?>(matrix1Data(), ChartType.MATRIX_HEATMAP)
        matrixHeatMap1 = MatrixPane<MatrixChartItem?>(matrixItemSeries1)
        matrixHeatMap1!!.setColorMapping(ColorMapping.INFRARED_1)
        matrixHeatMap1!!.matrix.setUseSpacer(false)
        matrixHeatMap1!!.matrix.setColsAndRows(NO_OF_CELLS, NO_OF_CELLS)
        matrixHeatMap1!!.setPrefSize(400.0, 400.0)
    }

    private fun matrix1Data(): MutableList<MatrixChartItem?> {
        var cellX: Int
        var cellY = 0
        val matrixData1: MutableList<MatrixChartItem?> = ArrayList()
        run {
            var y = 0.0
            while (y < TWO_PI) {
                cellX = 0
                var x = 0.0
                while (x < TWO_PI) {
                    matrixData1.add(
                        MatrixChartItem(
                            cellX,
                            cellY,
                            (cos(y * TWO_PI * 0.125) * sin(x * TWO_PI * 0.125) + 1) * 0.5
                        )
                    )
                    cellX++
                    x += STEP
                }
                cellY++
                y += STEP
            }
        }
        return matrixData1
    }

    private fun timer(): AnimationTimer {
        return object : AnimationTimer() {
            override fun handle(now: Long) {
                if (now > lastTimerCall + 10000000L) {
                    var cellX: Int
                    var cellY = 0
                    var y = 0.0
                    while (y < TWO_PI) {
                        if (factor.compareTo(Math.PI * 2.55) >= 0) {
                            factor = 0.0
                        }
                        cellX = 0
                        var x = factor
                        while (x < TWO_PI + factor) {
                            val variance: Double = abs(cos(x / 100.0) + (RND.nextDouble() - 0.5) / 10.0)
                            val value =
                                (cos(y * TWO_PI * 0.125) * sin(x * TWO_PI * 0.125) + 1) * 0.5 * variance
                            matrixHeatMap1!!.setValueAt(cellX, cellY, value)
                            cellX++
                            x += STEP
                        }
                        cellY++
                        y += STEP
                    }
                    matrixHeatMap1!!.matrix.drawMatrix()
                    factor += STEP
                    lastTimerCall = now
                }
            }
        }
    }

    override fun start(stage: Stage) {
        val pane = VBox(10.0, matrixHeatMap2)
        pane.padding = Insets(10.0)
        val scene = Scene(pane)
        stage.title = "MatrixHeatMap"
        stage.scene = scene
        stage.show()
        //timer!!.start()
    }

    override fun stop() {
        exitProcess(0)
    }

    companion object {
        private val RND: Random = Random()
        private const val TWO_PI = 2 * Math.PI
        private const val NO_OF_CELLS = 100
        private const val STEP = TWO_PI / NO_OF_CELLS

        @JvmStatic
        fun main(vararg args: String) {
            launch(PlotterCopy::class.java)
        }
    }
}