package at.ac.tuwien.trustcps.tracking.commands

class BoundsInitializer(labels: List<String>, cmdExec: (String) -> Unit) :
    BrowserCommand(cmdExec) {

    init {
        labels.forEach { cmdExec("""root.style.setProperty('--wm-$it', '')""") }
    }

    override fun dump(target: MutableMap<String, String>) {
        // do nothing
    }
}
