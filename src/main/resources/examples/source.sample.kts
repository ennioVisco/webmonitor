import at.ac.tuwien.trustcps.WebSource
import at.ac.tuwien.trustcps.tracking.Browser

// Unfortunately, the frame-size is browser-specific.
// Typically, all browsers have only a vertical frame,
// but it would be nice to set a window size based on the inner dimensions,
// not the external ones.
val verticalBrowserFrame = 133 // px, i.e. Chrome browser's offset

// We are targeting the Pixel 5 resolution
WebSource.screenWidth = 600 // px
WebSource.screenHeight = 500 // px

// To date Google Chrome is the browser with the most stable APIs
WebSource.browser = Browser.CHROME

WebSource.wait = 0

WebSource.maxSessionDuration = 5_000 // ms

WebSource.targetUrl = "https://enniovisco.github.io/webmonitor/"

