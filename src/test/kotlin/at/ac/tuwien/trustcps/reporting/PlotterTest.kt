package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.space.Grid
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import javafx.stage.Stage
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertFailsWith

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

    @Disabled("failing on mac")
    @Test
    fun `basic heatmap does not fail`() {
        val data = arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 0.0))
        val mockStage = mockk<Stage>()
        justRun { mockStage.title = any() }
        justRun { mockStage.scene = any() }

        val plotter = spyk(Plotter(0, "test", data, grid), recordPrivateCalls = true)
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
