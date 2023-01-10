import at.ac.tuwien.trustcps.*
import at.ac.tuwien.trustcps.tracking.Browser

// Unfortunately, the frame-size is browser-specific.
// Typically, all browsers have only a vertical frame,
// but it would be nice to set a window size based on the inner dimensions,
// not the external ones.
val verticalBrowserFrame = 133 // px, i.e. Chrome browser's offset

// We are targeting the Pixel 5 resolution
WebSource.screenWidth = 800 // px
WebSource.screenHeight = 600 + verticalBrowserFrame // px

// To date Google Chrome is the browser with the most stable APIs
WebSource.browser = Browser.CHROME

WebSource.maxSessionDuration = 55_000L // 15 secs

WebSource.targetUrl = "https://conf.researchr.org/home/ase-2022"

