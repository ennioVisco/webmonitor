package at.ac.tuwien.trustcps.tracking.commands

abstract class BrowserCommand {
    abstract fun dump(target: MutableMap<String, String>)
}
