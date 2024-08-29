import com.enniovisco.*
import com.enniovisco.dsl.Browser

WebSource.screenWidth = 800 // px
WebSource.screenHeight = 400 // px

// To date Google Chrome is the browser with the most stable APIs
WebSource.browser = Browser.CHROME

WebSource.wait = 11_000L // milliseconds

WebSource.maxSessionDuration = 15_000L // milliseconds

WebSource.targetUrl = "https://nytimes.com"
