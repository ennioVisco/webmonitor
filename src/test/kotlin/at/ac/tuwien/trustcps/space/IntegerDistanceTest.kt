package at.ac.tuwien.trustcps.space

import eu.quanticol.moonlight.domain.IntegerDomain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class IntegerDistanceTest {
    @Test fun `zero is actually zero`() {
        val domain = IntegerDomain()
        assertEquals(0, domain.zero())
    }

    @Test fun `infinity sums bring negatives`() {
        val domain = IntegerDomain()
        assertEquals(Int.MIN_VALUE, domain.sum(1, Int.MAX_VALUE))
    }

    @Test fun `simple inequalities`() {
        val domain = IntegerDomain()
        assertEquals(true, domain.lessOrEqual(1, 5))
        assertEquals(true, domain.lessOrEqual(5, 5))
    }
}