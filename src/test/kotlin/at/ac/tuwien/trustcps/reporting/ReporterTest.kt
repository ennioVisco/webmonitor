package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.evenLocationsAreTrueSignal
import at.ac.tuwien.trustcps.space.Grid
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

internal class ReporterTest {
    @Disabled("Needs way to mock display request")
    @Test fun `plotting somehow plots`() {
        val grid = Grid(2, 2)
        val reporter = Reporter(grid)
        val ss = mockk<SpatialTemporalSignal<Boolean>>()
        val ts = mockk<Signal<Boolean>>()
        every { ts.valueAt(0.0) } returns true
        every { ss.signals } returns listOf(ts)

        mockkConstructor(Plotter::class)
        every { anyConstructed<Plotter>().run() } returns Unit

        assertDoesNotThrow {
            reporter.plot(ss, "fake signal")
        }
    }

    @Nested inner class StandardOutput {
        @Test fun `marking prints the right text`() {
            val text = "test"
            val reporter = Reporter(mockk())

            val output = tapSystemOut {
                reporter.mark(text)
            }

            val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            assertEquals("[$currentTime] - $text", output.trim())
        }


        @Test fun `dumping spatio-temporal signals prints right output`() {
            val title = "test space-time trace"
            val grid = Grid(2, 2)
            val reporter = Reporter(grid)
            val ss = evenLocationsAreTrueSignal(4)

            val output = tapSystemOut {
                reporter.report(ss, title)
            }

            val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            assertEquals(signalDump1(currentTime), output)
        }

        private fun signalDump1(time: LocalDateTime): String {
            var text = timeBox(time, "test space-time trace")
            text += timeBox(time, 1.0)
            text += timeBox(time, -1.0)
            text += timeBox(time, 1.0)
            text += timeBox(time, -1.0)
            return text
        }

        @Test fun `dumping temporal signals prints right output`() {
            val title = "test time trace"
            val grid = Grid(2, 2)
            val reporter = Reporter(grid)
            val ss = evenLocationsAreTrueSignal(4)

            val output = tapSystemOut {
                reporter.report(ss.signals[0], title)
            }

            val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            assertEquals(signalDump2(currentTime), output)
        }

        private fun signalDump2(time: LocalDateTime): String {
            var text = timeBox(time, "test time trace")
            text += timeBox(time, 1.0)
            text += timeBox(time, 1.0)
            return text
        }

        private fun <T> timeBox(time: LocalDateTime, content: T) =
            "[$time] - ${content.toString()}${System.lineSeparator()}"
    }
}
