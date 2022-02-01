package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.space.Grid
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

internal class ReporterTest {
    @Test fun `marking prints the right text`() {
        val text = "test"
        val output = tapSystemOut {
            val reporter = Reporter(mockk())
            reporter.mark(text)
        }

        assertEquals("${text}...${Calendar.getInstance().time.seconds}",
                     output.trim())
    }

    @Disabled
    @Test fun `plotting somehow plots`() {
        val grid = Grid(2, 2)
        val reporter = Reporter(grid)
        val ss = mockk<SpatialTemporalSignal<Boolean>>()
        val ts = mockk<Signal<Boolean>>()
        every { ts.valueAt(0.0) } returns true
        every { ss.signals } returns listOf(ts)

        assertDoesNotThrow {
            reporter.plot(ss, "fake signal")
        }
    }
}