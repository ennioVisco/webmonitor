package at.ac.tuwien.trustcps

object Target {
    // Unfortunately, the frame-size is browser-specific.
    // Typically, all browsers have only a vertical frame,
    // but it would be nice to set a window size based on the inner dimensions,
    // not the external ones.
    private const val VERTICAL_BROWSER_FRAME = 133 // Chrome browser offset

    const val MAX_SESSION_DURATION = 5_000L

    const val screenWidth = 393
    const val screenHeight = 851 + VERTICAL_BROWSER_FRAME

    //const val targetUrl = "https://enniovisco.github.io/webmonitor/"
    const val targetUrl = "https://conf.researchr.org/home/ase-2022"
}
