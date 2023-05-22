package com.enniovisco.checking

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class BoxTest {
    @Test
    fun `box success`() {
        val box = Box(0, 0, 10, 10)
        assertEquals(0, box.minX)
        assertEquals(0, box.minY)
        assertEquals(10, box.maxX)
        assertEquals(10, box.maxY)
    }

    @Test
    fun `box from illegal strings throws exception`() {
        assertFailsWith<IllegalArgumentException>(
            "Non-numeric strings should not be allowed"
        ) {
            Box.from("", "0", "1", "1")
        }
    }

    @Test
    fun `box from non-interval parameters fails the requirements1`() {
        assertFailsWith<IllegalArgumentException>(
            "Non-interval parameters should brake the box contract"
        ) {
            Box(10, 0, 0, 10)
        }
    }

    @Test
    fun `box from non-interval parameters fails the requirements2`() {
        assertFailsWith<IllegalArgumentException>(
            "Non-interval parameters should brake the box contract"
        ) {
            Box(0, 10, 10, 0)
        }
    }

    @Test
    fun `containment equality`() {
        val box = Box(0, 0, 10, 10)
        assertEquals(box.contains(5, 5), box.contains(Pair(5, 5)))
        assertEquals(box.contains(11, 11), box.contains(Pair(11, 11)))
    }

    @Test
    fun `what should be contained is contained`() {
        val box = Box(0, 0, 10, 10)
        assertTrue(box.contains(5, 5))
    }

    @Test
    fun `what should not be contained is not contained1`() {
        val box = Box(0, 0, 10, 10)
        assertFalse(box.contains(9, 11))
    }

    @Test
    fun `what should not be contained is not contained2`() {
        val box = Box(0, 0, 10, 10)
        assertFalse(box.contains(11, 9))
    }

    @Test
    fun `what should not be contained is not contained3`() {
        val box = Box(0, 0, 10, 10)
        assertFalse(box.contains(-1, 9))
    }

    @Test
    fun `what should not be contained is not contained4`() {
        val box = Box(0, 0, 10, 10)
        assertFalse(box.contains(0, -1))
    }
}
