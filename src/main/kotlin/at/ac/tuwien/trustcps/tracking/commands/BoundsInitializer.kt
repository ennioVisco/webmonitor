package at.ac.tuwien.trustcps.tracking.commands

const val BOUNDS_PREFIX = "wm-"

class BoundsInitializer(labels: List<String>, cmdExec: (String) -> Unit) :
    BrowserCommand(cmdExec) {

    init {
        labels.forEach {
            cmdExec("document.querySelector(':root').style.setProperty('--$BOUNDS_PREFIX$it', '')")
            println("Initializing bound: $it")
        }
    }

    override fun dump(target: MutableMap<String, String>) {
        // do nothing
    }
}
