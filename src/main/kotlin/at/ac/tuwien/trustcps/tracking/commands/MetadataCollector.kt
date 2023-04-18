package at.ac.tuwien.trustcps.tracking.commands

import mu.*

class MetadataCollector(cmdExec: (String) -> Any) : BrowserCommand() {
    private val layoutVpWidth: String
    private val layoutVpHeight: String
    private val visualVpWidth: String
    private val visualVpHeight: String
    private val log = KotlinLogging.logger {}

    init {
        /*
           Document areas considered by the page, including unreachable ones.
           They are not tracked at all at the moment
        */
        // Layout Viewport: Scrollable area
        val layoutVp = "return document.documentElement"
        layoutVpWidth = cmdExec("${layoutVp}.scrollWidth").toString()
        layoutVpHeight = cmdExec("${layoutVp}.scrollHeight").toString()
        log.info("Layout Viewport: ${layoutVpWidth}x${layoutVpHeight}")

        // Visual Viewport: physical screen
        visualVpWidth = cmdExec("return window.innerWidth;").toString()
        visualVpHeight = cmdExec("return window.innerHeight;").toString()
        log.info("Visual Viewport: ${visualVpWidth}x${visualVpHeight}")
    }

    override fun dump(target: MutableMap<String, String>) {
        target["lvp_width"] = layoutVpWidth
        target["lvp_height"] = layoutVpHeight
        target["vvp_width"] = visualVpWidth
        target["vvp_height"] = visualVpHeight
    }

}
