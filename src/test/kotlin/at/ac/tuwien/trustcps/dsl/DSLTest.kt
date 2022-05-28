package at.ac.tuwien.trustcps.dsl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DSLTest {

    @Test fun `events can be defined without an element selector`() {
        val event = Event("click")
        
        assertEquals(event.selector, document)
    }

    @Test fun `events as pair give correct results`() {
        val event = Event("click", document)
        
        val pair = event.asPair()
        
        assertEquals(event.eventName, pair.second)
        assertEquals(event.selector.queryString, pair.first)
    }
    
    @Test fun `selectors `() {
        
    }
}
