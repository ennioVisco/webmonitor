package com.enniovisco.reporting

import com.enniovisco.*
import com.enniovisco.space.*
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import eu.quanticol.moonlight.offline.signal.*
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.*
import java.time.temporal.*
import kotlin.test.*

internal class ReporterTest {
    @Test
    fun `plotting somehow plots`() {
        val id = 0
        val title = "fake signal"
        val grid = Grid(2, 2)
        val reporter = spyk(Reporter(), recordPrivateCalls = true)
        val ss = mockk<SpatialTemporalSignal<Boolean>>()
        val ts = mockk<Signal<Boolean>>()
        every { ts.getValueAt(any()) } returns true
        every { ss.signals } returns listOf(ts)

        justRun {
            reporter invoke "spawnPlotter" withArguments
                    listOf(id, title, (any<Array<DoubleArray>>()), grid)
        }

        assertDoesNotThrow {
            reporter.plot(id, ss, grid, title)
        }
    }

    @Test
    fun `illegal types in signals throw illegalArgumentException`() {
        val reporter = Reporter()
        val s = stringSignal()

        assertFailsWith<IllegalArgumentException> {
            reporter.report(s, "any")
        }
    }

    @Test
    fun `boolean types in signals are supported`() {
        val reporter = Reporter()
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
        val reporter = Reporter()
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
            val reporter = Reporter(toConsole = true)

            val output = tapSystemOut {
                reporter.mark(text)
            }

            val currTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
            assertEquals(
                "$currTime [Test worker] \u001B[34mINFO \u001B[0;39m Reporter - ==== $text ====",
                output.trim()
            )
        }


        @Test
        fun `dumping spatio-temporal signals prints right output`() {
            val title = "test space-time trace"
            val reporter = Reporter(
                toConsole = true,
                logTimeGranularity = ChronoUnit.MINUTES
            )
            val ss = evenLocationsAreTrueSignal(4)

            val output = tapSystemOutNormalized {
                reporter.report(ss, title)
            }

            val currTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            assertEquals(signalDump1(currTime), output)
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
            val reporter = Reporter(
                toConsole = true,
                logTimeGranularity = ChronoUnit.MINUTES
            )
            val ss = evenLocationsAreTrueSignal(4)

            val output = tapSystemOutNormalized {
                reporter.report(ss.signals[0], title)
            }

            val currTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            assertEquals(signalDump2(currTime), output)
        }

        private fun signalDump2(time: LocalDateTime): String {
            var text = timeBox(time, "test time trace")
            text += timeBox(time, 1.0)
            text += timeBox(time, 1.0)
            return text
        }

        private fun <T> timeBox(time: LocalDateTime, content: T) =
            "[$time] - ${content.toString()}\n"
    }
}
