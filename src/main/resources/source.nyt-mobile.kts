import com.enniovisco.*

// We are targeting the Pixel 5 resolution
WebSource.screenWidth = 393 // px
WebSource.screenHeight = 851 // px
//WebSource.screenWidth = 800 // px
//WebSource.screenHeight = 600 // px


WebSource.browser = Browser.CHROME

WebSource.wait = 3_000 // ms

WebSource.maxSessionDuration = 20_000 // ms

WebSource.targetUrl = "https://www.nytimes.com"
/* Note: to get the same result, it is important that the js scripts active
    at the time of these experiments are the same.
    Alternatively, a close permanent copy of the website is available in the
    internet archive. To use it, uncomment the following line:
*/
// WebSource.targetUrl = "https://web.archive.org/web/20230503145309/https://www.nytimes.com/"
