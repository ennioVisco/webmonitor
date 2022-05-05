import org.researchr.conf.ase2022.WebSource
import org.researchr.conf.ase2022.tracking.Browser

// Unfortunately, the frame-size is browser-specific.
// Typically, all browsers have only a vertical frame,
// but it would be nice to set a window size based on the inner dimensions,
// not the external ones.
val verticalBrowserFrame = 133 // px, i.e. Chrome browser's offset

// We are targeting the resolution of a small window
WebSource.screenWidth = 800 // px
WebSource.screenHeight = 600 + verticalBrowserFrame // px

// To date Google Chrome is the browser with the most stable APIs
WebSource.browser = Browser.FIREFOX

WebSource.maxSessionDuration = 125_000L // 125 secs

WebSource.targetUrl = "https://conf.researchr.org/home/ase-2022"

