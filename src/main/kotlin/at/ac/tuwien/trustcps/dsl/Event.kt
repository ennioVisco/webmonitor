package at.ac.tuwien.trustcps.dsl

/**
 * Class including all the info for Javascript event definition 
 */
data class Event(val eventName: String, 
                 val selector: Selector = document) {
    infix fun on(selector: Selector): Event {
        return Event(this.eventName, selector)
    }

    fun asPair(): Pair<String, String> {
        return Pair(selector.queryString, eventName)
    }
}
