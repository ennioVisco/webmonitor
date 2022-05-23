package at.ac.tuwien.trustcps.parsing

class Event(private val eventName: String, var selector: Selector? = null) {
    infix fun on(selector: Selector): Event {
        this.selector = selector
        return this
    }

    fun asPair(): Pair<String, String> {
        return if (selector != null) {
            Pair(selector!!.queryString, eventName)
        } else {
            Pair("document", eventName)
        }
    }
}
