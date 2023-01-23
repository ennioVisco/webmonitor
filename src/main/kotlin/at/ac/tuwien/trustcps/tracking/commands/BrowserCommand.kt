package at.ac.tuwien.trustcps.tracking.commands

abstract class BrowserCommand(protected val cmdExec: (String) -> Any) {
    abstract fun dump(target: MutableMap<String, String>)
}
