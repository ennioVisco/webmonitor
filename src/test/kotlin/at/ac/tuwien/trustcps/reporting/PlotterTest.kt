package at.ac.tuwien.trustcps.reporting

import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class PlotterTest {
    @Test fun `classic JavaFX API is not allowed`() {
        val plotter = Plotter("test", arrayOf(doubleArrayOf(0.0)))

        assertFailsWith<UnsupportedOperationException> { plotter.init() }
        assertFailsWith<UnsupportedOperationException> {
            plotter.start(mockk())
        }
        assertFailsWith<UnsupportedOperationException> { plotter.stop() }
    }

//    @Test fun `plot basic heatmap`() {
//        val data = arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(1.0, 0.0))
//        val plotter = Plotter("test", data)
//        plotter.run()
//    }
}
