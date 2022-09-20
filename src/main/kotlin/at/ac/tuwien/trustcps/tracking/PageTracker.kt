package at.ac.tuwien.trustcps.tracking

import mu.*
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
    private val browser: Browser? = null,
    private val maxSessionDuration: Long = 100,
    private val wait: Long = 1_000L,
    private val toFile: Boolean = false
) {
    private val prefix: String = "[wm] "
    private val snapshots = mutableListOf<Map<String, String>>()
    private val selectors = ArrayList<String>()
    private val events = ArrayList<Pair<String, String>>()
    private var snapshotBuilder: SnapshotBuilder? = null
    private val log = KotlinLogging.logger {}

    /**
     * lazily selects some elements to track from the page
     */
    fun select(queryString: String) {
        selectors.add(queryString)
    }

    /**
     * lazily selects some events to track from the page
     */
    fun record(event: Pair<String, String>) {
        events.add(event)
    }

    /**
     * Tracks the provided selectors for the current page
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
        val recorder = EventRecorder(driver, prefix)
        recorder.capturePageLoaded()
        events.forEach { recorder.captureEvent(it.first, it.second) }
    }

    private fun takeSnapshot(event: ConsoleEvent) {
        Thread.sleep(wait)
        if (event.messages[0].startsWith(prefix)) {
            log.info("Console log message is: ${event.messages}")
            snapshotOrFail {
                snapshotBuilder?.collect(snapshots.size)?.let {
                    snapshots.add(it)
                }
            }
        }
    }

    private fun snapshotOrFail(action: () -> Unit) {
        if (snapshotBuilder != null) {
            action()
        } else {
            throw UnsupportedOperationException(
                "Trying to capture event before instantiation is complete"
            )
        }
    }

    private fun spawnBrowserSession() =
        if (browser != null)
            SessionBuilder(page, ::takeSnapshot, dimension, browser)
        else
            SessionBuilder(page, ::takeSnapshot, dimension)

    fun isTracking(queryString: String) = selectors.contains(queryString)

    fun isRecordingAt(event: Pair<String, String>) = events.contains(event)
}
