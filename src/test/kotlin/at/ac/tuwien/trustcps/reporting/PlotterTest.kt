package at.ac.tuwien.trustcps.reporting

import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import javafx.application.Platform
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertFailsWith

internal class PlotterTest {
    @Test fun `classic JavaFX init is not allowed`() {
        val plotter = Plotter("test", arrayOf(doubleArrayOf(0.0)))

        assertFailsWith<UnsupportedOperationException> { plotter.init() }
    }

    @Test fun `classic JavaFX start is not allowed`() {
        val plotter = Plotter("test", arrayOf(doubleArrayOf(0.0)))

        assertFailsWith<UnsupportedOperationException> {
            plotter.start(mockk())
        }
    }

    @Test fun `classic JavaFX stop is not allowed`() {
        val plotter = Plotter("test", arrayOf(doubleArrayOf(0.0)))

        assertFailsWith<UnsupportedOperationException> { plotter.stop() }
    }

    @Test fun `basic heatmap does not fail`() {
        val data = arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 0.0))
        val plotter = spyk(Plotter("test", data), recordPrivateCalls = true)
        justRun { plotter invoke "showStage" withArguments listOf(any<Stage>()) }

        assertDoesNotThrow {
            Platform.startup {
                plotter.run()
            }
        }
    }
}
