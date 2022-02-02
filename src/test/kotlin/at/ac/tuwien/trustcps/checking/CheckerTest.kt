package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.evenLocationsAreTrueSignal
import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.formula.AtomicFormula
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class CheckerTest {
    private val grid = Grid(rows = 2, columns = 3)

    @Test fun `simple checking gives correct results`() {
        val data = mapOf("wnd_width" to "3", "wnd_height" to "2")
        val checker = Checker(grid, listOf(data), emptyList())

        val result = checker.check(AtomicFormula("screen"))

        assertEquals(6, result.signals.size)
        result.signals.forEach {
            assertEquals(1, it.size())
            assertTrue(it.valueAt(0.0))
        }
    }

    @Test fun `checking empty data fails`() {
        val checker = Checker(grid, emptyList(), emptyList())
        assertFailsWith<IllegalArgumentException>(
            "Empty data in input should not be allowed") {
            checker.check(AtomicFormula("screen"))
        }
    }

    @Test fun `atoms are right`() {
        val checker = Checker(grid, emptyList(), listOf("elem"))
        val elem = checker.atoms["elem"]!!

        assertEquals(true,
                     elem.apply(null).apply(listOf(false, true)))
    }
}
