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

    @Test
    fun `classic JavaFX init is not allowed`() {
        val plotter = Plotter(0, "test", arrayOf(doubleArrayOf(0.0)), grid)

        assertFailsWith<UnsupportedOperationException> { plotter.init() }
    }

    @Test
    fun `classic JavaFX start is not allowed`() {
        val plotter = Plotter(0, "test", arrayOf(doubleArrayOf(0.0)), grid)

        assertFailsWith<UnsupportedOperationException> {
            plotter.start(mockk())
        }

    }

    @Test
    fun `classic JavaFX stop is not allowed`() {
        val plotter = Plotter(0, "test", arrayOf(doubleArrayOf(0.0)), grid)

        assertFailsWith<UnsupportedOperationException> { plotter.stop() }
    }

    @Test
    fun `plotter plots something`() {
        val data = arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 0.0))
        val plotter = Plotter(0, "test", data, grid)

        assertDoesNotThrow {
            Platform.startup(plotter)
        }
    }

    @Disabled("failing on mac")
    @Test
    fun `basic heatmap does not fail`() {
        System.setProperty("org.graphstream.debug", "true")
        val data = arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 0.0))
        val mockStage = mockk<Stage>()
        justRun { mockStage.title = any() }
        justRun { mockStage.scene = any() }

        val plotterSkeleton = Plotter(0, "test", data, grid)
        val plotter = spyk(plotterSkeleton, recordPrivateCalls = true)
        every {
            plotter invoke "spawnStage" withArguments
                    listOf()
        } returns mockStage
        justRun {
            plotter invoke "showStage" withArguments
                    listOf(any<Stage>())
        }

        assertDoesNotThrow {
            plotter.run()
        }
    }
}
