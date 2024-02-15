package com.enniovisco.checking

import com.enniovisco.space.*
import io.github.moonlightsuite.moonlight.formula.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class CheckerTest {
    private val grid = Grid(rows = 2, columns = 3)

    @Test
    fun `simple checking gives correct results`() {
        val data = mapOf("vvp_width" to "3", "vvp_height" to "2")
        val checker = Checker(grid, listOf(data), emptyMap())

        val result = checker.check(AtomicFormula("screen"))

        assertEquals(6, result.signals.size)
        result.signals.forEach {
            assertEquals(1, it.size())
            assertTrue(it.getValueAt(0.0))
        }
    }

    @Test
    fun `checking empty data fails`() {
        val checker = Checker(grid, emptyList(), emptyMap())
        assertFailsWith<IllegalArgumentException>(
            "Empty data in input should not be allowed"
        ) {
            val output = checker.check(AtomicFormula("screen"))
            println(output)
        }
    }

    @Test
    fun `atoms are right`() {
        val dummy = mapOf("elem" to { _: String, _: String -> true })
        val checker =
            Checker(grid, emptyList(), dummy)
        val elem = checker.atoms["elem"]!!

        assertEquals(
            true,
            elem.apply(null).apply(listOf(false, true))
        )
    }
}
