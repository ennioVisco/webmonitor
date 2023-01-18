import at.ac.tuwien.trustcps.*

// We are targeting the Pixel 5 resolution
WebSource.screenWidth = 500 // px
WebSource.screenHeight = 520 // px

// To date Google Chrome is the browser with the most stable APIs
WebSource.browser = Browser.CHROME_HEADLESS

WebSource.wait = 0

WebSource.maxSessionDuration = 5_000 // ms

WebSource.targetUrl = "https://enniovisco.github.io/webmonitor/sample.html"

