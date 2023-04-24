package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.space.*
import io.mockk.*
import javafx.application.*
import javafx.stage.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*


internal class PlotterTest {
    private val grid = Grid(3, 3)

    private val data = arrayOf(
        doubleArrayOf(0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0),
        doubleArrayOf(0.0, 0.0, 0.0)
    )

    @Test
    fun `classic JavaFX init is not allowed`() {
        val plotter =
            Plotter(0, "test", data, grid, 1.0)

        assertFailsWith<UnsupportedOperationException> { plotter.init() }
    }

    @Test
    fun `classic JavaFX start is not allowed`() {
        val plotter = Plotter(0, "test", data, grid, 1.0)

        assertFailsWith<UnsupportedOperationException> {
            plotter.start(mockk())
        }

    }

    @Test
    fun `classic JavaFX stop is not allowed`() {
        val plotter = Plotter(0, "test", data, grid, 1.0)

        assertFailsWith<UnsupportedOperationException> { plotter.stop() }
    }

    @Test
    fun `plotter plots something`() {
        val plotter = Plotter(0, "test", data, grid, 1.0)

        assertDoesNotThrow("Are you sure you have a display manager?") {
            Platform.runLater(plotter)
        }
    }


    companion object {
        @BeforeAll
        @JvmStatic
        fun `init JavaFX`() {
            Platform.setImplicitExit(false)
            Platform.startup { }
        }

        @AfterAll
        @JvmStatic
        fun `shutdown JavaFX`() {
            Platform.exit()
        }
    }

    @Test
    fun `basic heatmap does not fail`() {
        System.setProperty("org.graphstream.debug", "true")
        val data = arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 0.0))
        val mockStage = mockk<Stage>()
        justRun { mockStage.title = any() }
        justRun { mockStage.scene = any() }

        assertDoesNotThrow {
            Platform.runLater { plotterStub(mockStage, data) }
        }
    }

    private fun plotterStub(stage: Stage, data: Array<DoubleArray>): Plotter {
        val plotterSkeleton = Plotter(0, "test", data, grid, 1.0)
        val plotter = spyk(plotterSkeleton, recordPrivateCalls = true)
        every {
            plotter invoke "spawnStage" withArguments
                    listOf()
        } returns stage
        justRun {
            plotter invoke "showStage" withArguments
                    listOf(any<Stage>())
        }
        return plotter
    }
}
