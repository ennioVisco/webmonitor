package com.enniovisco.dsl

import io.github.moonlightsuite.moonlight.formula.*
import io.github.moonlightsuite.moonlight.formula.classic.*
import io.github.moonlightsuite.moonlight.formula.spatial.*
import io.github.moonlightsuite.moonlight.formula.temporal.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class OperatorsTest {

    @Test
    fun `it builds exactly the same formulae`() {
        val x = AtomicFormula("x")
        val y = AtomicFormula("y")

        assertEquals(not(x or y), NegationFormula(OrFormula(x, y)))
        assertEquals(not(x and y), NegationFormula(AndFormula(x, y)))
        assertEquals(
            not(x implies y),
            NegationFormula(OrFormula(NegationFormula(x), y))
        )

        assertEquals(
            eventually(globally(x)),
            EventuallyFormula(GloballyFormula(x))
        )

        assertEquals(x until y, UntilFormula(x, y))

        assertEquals(
            everywhere(somewhere(x)),
            EverywhereFormula(
                Spec.basicDistance,
                SomewhereFormula(Spec.basicDistance, x)
            )
        )

        assertEquals(x reach y, ReachFormula(x, Spec.basicDistance, y))

    }
}
