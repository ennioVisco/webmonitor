import com.enniovisco.*

// We are targeting the Pixel 5 resolution
WebSource.screenWidth = 393 // px
WebSource.screenHeight = 851 // px

// To date Google Chrome is the browser with the most stable APIs
WebSource.browser = Browser.CHROME_HEADLESS

WebSource.wait = 0

WebSource.maxSessionDuration = 5_000 // ms

WebSource.targetUrl = "https://enniovisco.github.io/webmonitor/sample.html"
//WebSource.targetUrl = "http://localhost/sample.html"

