package at.ac.tuwien.trustcps.dsl

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class DSLTest {
    @Nested
    inner class Events {
        @Test
        fun `can be defined without an element selector`() {
            val event = Event("click")

            assertEquals(event.selector, document)
        }

        @Test
        fun `as pair give correct results`() {
            val event = Event("click")

            val (queryString, name) = event.asPair()

            assertEquals(event.eventName, name)
            assertEquals(event.selector.queryString, queryString)
        }


    }

    @Nested
    inner class Selectors {
        @Test
        fun `trivial selector`() {
            val selector = select { "body" }

            assertEquals("body", selector.toString())
        }

        @Test
        fun `read attribute without comparison is ignored`() {
            val selector = select { "body" } read "attribute"

            assertEquals("body", selector.toString())
        }

        @Test
        fun `read attribute with comparison`() {
            val selector1 = select { "body" } read "attr" equalTo "0"
            val selector2 = select { "body" } read "attr" greaterThan "0"
            val selector3 = select { "body" } read "attr" lessThan "0"
            val selector4 = select { "body" } read "attr" greaterEqualThan "0"
            val selector5 = select { "body" } read "attr" lessEqualThan "0"

            assertEquals("body\$attr = 0", selector1.toString())
            assertEquals("body\$attr > 0", selector2.toString())
            assertEquals("body\$attr < 0", selector3.toString())
            assertEquals("body\$attr >= 0", selector4.toString())
            assertEquals("body\$attr <= 0", selector5.toString())
        }

        @Test
        fun `attribute can be bound`() {
            val selector = select { "body" } read "attr" bind "label"

            assertEquals("body\$attr & label", selector.toString())
        }
    }

    @Test
    fun `events can be defined on on selectors`() {
        val someSelectorId = "body"
        val event = Event("click") on select { someSelectorId }

        val (queryString, name) = event.asPair()

        assertEquals(event.eventName, name)
        assertEquals(someSelectorId, queryString)
    }
}
