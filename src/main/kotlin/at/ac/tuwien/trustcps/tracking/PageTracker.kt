package at.ac.tuwien.trustcps.tracking

import org.openqa.selenium.*
import org.openqa.selenium.devtools.events.*
import org.openqa.selenium.remote.*
import java.net.*


/**
 * Tracks key elements of the target page.
 * @param page is the url to track
 * @param dimension sets the dimensions of the browser window
 * @param browser selects the browser to run the tracking session
 */
class PageTracker(
    private val page: URL,
    private val dimension: Dimension? = null,
    private val browser: Browser,
    private val maxSessionDuration: Long = 100,
    private val wait: Long = 1_000L,
    private val toFile: Boolean = false
) {
    private val snapshots = mutableListOf<Map<String, String>>()
    private val selectors = ArrayList<String>()
    private val events = ArrayList<Pair<String, String>>()
    private var snapshotBuilder: SnapshotBuilder? = null

    /**
     * selects some elements to track from the page
     */
    fun select(queryString: String) {
        selectors.add(queryString)
    }

    /**
     * selects some elements to track from the page
     */
    fun record(event: Pair<String, String>) {
        events.add(event)
    }

    /**
     * Tracks the provided selectors for the provided page
     */
    fun run(): List<Map<String, String>> {
        spawnBrowserSession().use {
            snapshotBuilder = SnapshotBuilder(it.driver, selectors, toFile)
            recordEvents(it.driver)
            Thread.sleep(maxSessionDuration)
        }
        return snapshots
    }

    private fun recordEvents(driver: RemoteWebDriver) {
        capturePageLoaded(driver)
        events.forEach { captureEvent(driver, it.first, it.second) }
        //driver.executeScript("\$('div[data-ride=\"carousel\"').on('slide.bs.carousel', function () { console.log(\"carousel-slide\")})")
    }

    private fun captureEvent(driver: RemoteWebDriver, selector: String, event: String) {
        driver.executeScript(
            "$selector.addEventListener('$event', () => { " +
                    "console.log('[wm] $selector@$event'); });"
        )
    }

    private fun capturePageLoaded(driver: RemoteWebDriver) {
        driver.executeScript(
            "if(document.readyState === \"complete\") {" +
                    "console.log('[wm] page is fully loaded');" +
                    "} else {" +
                    "window.addEventListener('load', () => { console.log" +
                    "('[wm] page is fully loaded'); " +
                    "})} "
        )
    }

    private fun capture(event: ConsoleEvent) {
        Thread.sleep(wait)
        if (event.messages[0].startsWith("[wm] ")) {
            if (snapshotBuilder != null) {
                println("Console log message is ${event.messages}")
                snapshotBuilder?.collect(snapshots.size)
                    ?.let { snapshots.add(it) }
            } else {
                throw UnsupportedOperationException("Trying to capture event before instantiation is complete")
            }
        }

    }

    private fun spawnBrowserSession() =
        SessionBuilder(page, ::capture, dimension, browser)


//    fun selectAll(driver: RemoteWebDriver) {
//        driver.findElements(By.xpath("//*")).forEach { elem ->
//            println(elem.rect.toString())
//        }
//    }
//
//    fun execScript(driver: RemoteWebDriver) {
//        //val viewport = driver.executeScript("return [window.innerWidth, window.innerHeight];")
//        val h12 = driver.executeScript(
//            "return window.getComputedStyle(document.querySelector('h1')).getPropertyValue('font-size');")
//        println("<h1>'s font-size: $h12")
//    }

}
