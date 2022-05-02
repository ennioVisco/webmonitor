package at.ac.tuwien.trustcps.tracking

import org.openqa.selenium.Dimension
import org.openqa.selenium.devtools.events.ConsoleEvent
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL


/**
 * Tracks key elements of the target page.
 * @param page is the url to track
 * @param dimension sets the dimensions of the browser window
 * @param browser selects the browser to run the tracking session
 */
class PageTracker(
    private val page: URL,
    private val dimension: Dimension? = null,
    private val browser: Browser? = null,
    private val maxSessionDuration: Long = 100,
    private val toFile: Boolean = false
) {
    private val snapshots = mutableListOf<Map<String, String>>()
    private val selectors = ArrayList<String>()
    private var snapshotBuilder: SnapshotBuilder? = null

    /**
     * selects some elements to track from the page
     */
    fun select(queryString: String) {
        selectors.add(queryString)
    }

    /**
     * Tracks the provided selectors for the provided page
     */
    fun track(): List<Map<String, String>> {
        spawnBrowserSession().use {
            snapshotBuilder = SnapshotBuilder(it.driver, selectors, toFile)
            recordEvents(it.driver)
            Thread.sleep(maxSessionDuration)
        }
        return snapshots
    }

    private fun recordEvents(driver: RemoteWebDriver) {
        capturePageLoaded(driver)
        driver.executeScript("\$('div[data-ride=\"carousel\"').on('slide.bs.carousel', function () { console.log(\"carousel-slide\")})")
    }

    private fun capturePageLoaded(driver: RemoteWebDriver) {
        driver.executeScript(
            "if(document.readyState === \"complete\") {" +
                    "console.log('page is fully loaded');" +
                    "} else {" +
                    "window.addEventListener('load', () => { console.log('page is fully loaded'); " +
                    "})} "
        )
    }

    private fun capture(event: ConsoleEvent) {
        Thread.sleep(1_000L)
        println("Console log message is ${event.messages}")
        if (snapshotBuilder != null) {
            snapshotBuilder?.collect(snapshots.size)?.let { snapshots.add(it) }
        } else {
            throw UnsupportedOperationException(
                "Trying to capture event before instantiation is complete"
            )
        }

    }

    private fun spawnBrowserSession() = SessionBuilder(page, ::capture, dimension, browser)


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