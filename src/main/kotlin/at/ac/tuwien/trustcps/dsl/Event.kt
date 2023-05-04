package at.ac.tuwien.trustcps.dsl

fun every(milliseconds: Int): Event {
    return Event("$milliseconds", Selector("Timer"))
}

/**
 * Class including all the info for Javascript event definition
 */
data class Event(
    val eventName: String,
    val selector: Selector = document
) {
    infix fun on(selector: Selector): Event {
        return Event(this.eventName, selector)
    }

    infix fun everyMillis(milliseconds: Int): Event {
        return Event(this.eventName, Selector("Timer"))
    }

    fun asPair(): Pair<String, String> {
        return Pair(selector.queryString, eventName)
    }
}
