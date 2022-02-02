package at.ac.tuwien.trustcps.checking

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class BoxTest {
    @Test fun `box success`() {
        val box = Box(0, 0, 10, 10)
        assertEquals(0, box.minX)
        assertEquals(0, box.minY)
        assertEquals(10, box.maxX)
        assertEquals(10, box.maxY)
    }

    @Test fun `box from illegal strings throws exception`() {
        assertFailsWith<IllegalArgumentException>(
            "Non-numeric strings should not be allowed") {
            Box.from("", "0", "1", "1")
        }
    }

    @Test fun `box from non-interval parameters fails the requirements`() {
        assertFailsWith<IllegalArgumentException>(
            "Non-interval parameters should brake the box contract") {
            Box(10, 0, 0, 10)
        }
    }

    @Test fun `containment equality`() {
        val box = Box(0, 0, 10, 10)
        assertEquals(box.contains(5, 5), box.contains(Pair(5, 5)))
        assertEquals(box.contains(11, 11), box.contains(Pair(11, 11)))
    }

    @Test fun `what should be contained is contained`() {
        val box = Box(0, 0, 10, 10)
        assertTrue(box.contains(5, 5))
    }

    @Test fun `what should not be contained is not contained`() {
        val box = Box(0, 0, 10, 10)
        assertFalse(box.contains(11, 11))
    }
}