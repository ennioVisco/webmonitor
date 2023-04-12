package at.ac.tuwien.trustcps.tracking

import org.junit.jupiter.api.*
import org.openqa.selenium.*
import java.net.*
import kotlin.test.*
import kotlin.test.Test

class PageTrackerTest {
    private val url = URL("https://www.google.com")

    @Test
    fun `test can track basic selectors`() {
        val tracker = PageTracker(url)
        val queryString = "input[name='q']"

        tracker.select(queryString)

        assertTrue { tracker.isTracking(queryString) }
    }

    @Test
    fun `test can track basic events`() {
        val tracker = PageTracker(url)
        val event = Pair("input[name='q']", "click")

        tracker.record(event)

        assertTrue { tracker.isRecordingAt(event) }
    }

    @Test
    fun `trivial session runs`() {
        val tracker = trackerStub()

        assertDoesNotThrow { tracker.run() }
    }

    private fun trackerStub() = PageTracker(
        url,
        Dimension(100, 100),
        maxSessionDuration = 250,
        wait = 0
    )
}
