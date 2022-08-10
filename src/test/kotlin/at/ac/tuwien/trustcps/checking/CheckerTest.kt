package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.*
import eu.quanticol.moonlight.formula.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class CheckerTest {
    private val grid = Grid(rows = 2, columns = 3)

    @Test
    fun `simple checking gives correct results`() {
        val data = mapOf("vvp_width" to "3", "vvp_height" to "2")
        val checker = Checker(grid, listOf(data), emptyList())

        val result = checker.check(AtomicFormula("screen"))

        assertEquals(6, result.signals.size)
        result.signals.forEach {
            assertEquals(1, it.size())
            assertTrue(it.getValueAt(0.0))
        }
    }

    @Test
    fun `checking empty data fails`() {
        val checker = Checker(grid, emptyList(), emptyList())
        assertFailsWith<IllegalArgumentException>(
            "Empty data in input should not be allowed"
        ) {
            val output = checker.check(AtomicFormula("screen"))
            println(output)
        }
    }

    @Test
    fun `atoms are right`() {
        val checker = Checker(grid, emptyList(), listOf("elem"))
        val elem = checker.atoms["elem"]!!

        assertEquals(
            true,
            elem.apply(null).apply(listOf(false, true))
        )
    }
}
