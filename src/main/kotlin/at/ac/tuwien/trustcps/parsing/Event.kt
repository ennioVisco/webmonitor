package at.ac.tuwien.trustcps.parsing

class Event(val eventName: String, var selector: Selector? = null) {
    infix fun on(selector: Selector): Event {
        this.selector = selector
        return this
    }
}
