package at.ac.tuwien.trustcps.reporting

import at.ac.tuwien.trustcps.evenLocationsAreTrueSignal
import at.ac.tuwien.trustcps.space.Grid
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.test.assertFailsWith

internal class ReporterTest {
    @Test
    fun `plotting somehow plots`() {
        val title = "fake signal"
        val grid = Grid(2, 2)
        val reporter = spyk(Reporter(grid), recordPrivateCalls = true)
        val ss = mockk<SpatialTemporalSignal<Boolean>>()
        val ts = mockk<Signal<Boolean>>()
        every { ts.getValueAt(any()) } returns true
        every { ss.signals } returns listOf(ts)

        justRun {
            reporter invoke "spawnPlotter" withArguments
                    listOf(title, (any<Array<DoubleArray>>()))
        }

        assertDoesNotThrow {
            reporter.plot(ss, title)
        }
    }

    @Test
    fun `illegal types in signals throw illegalArgumentException`() {
        val reporter = Reporter(Grid(2, 2))
        val s = stringSignal()

        assertFailsWith<IllegalArgumentException> {
            reporter.report(s, "any")
        }
    }

    @Test
    fun `boolean types in signals are supported`() {
        val reporter = Reporter(Grid(2, 2))
        val s = booleanSignal()

        assertDoesNotThrow {
            reporter.report(s, "any")
        }
    }

    private fun booleanSignal(): Signal<Boolean> {
        val signal = Signal<Boolean>()
        signal.add(0.0, true)
        signal.add(1.0, false)
        return signal
    }

    @Test
    fun `double types in signals are supported`() {
        val reporter = Reporter(Grid(2, 2))
        val s = doubleSignal()

        assertDoesNotThrow {
            reporter.report(s, "any")
        }
    }

    private fun doubleSignal(): Signal<Double> {
        val signal = Signal<Double>()
        signal.add(0.0, 1.0)
        return signal
    }

    private fun stringSignal(): Signal<String> {
        val signal = Signal<String>()
        signal.add(0.0, "test")
        return signal
    }

    @Nested
    inner class StandardOutput {
        @Test
        fun `marking prints the right text`() {
            val text = "test"
            val reporter = Reporter(mockk(), true)

            val output = tapSystemOut {
                reporter.mark(text)
            }

            val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            assertEquals("[$currentTime] - $text", output.trim())
        }


        @Test
        fun `dumping spatio-temporal signals prints right output`() {
            val title = "test space-time trace"
            val grid = Grid(2, 2)
            val reporter = Reporter(grid, true)
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

        @Test
        fun `dumping temporal signals prints right output`() {
            val title = "test time trace"
            val grid = Grid(2, 2)
            val reporter = Reporter(grid, true)
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
