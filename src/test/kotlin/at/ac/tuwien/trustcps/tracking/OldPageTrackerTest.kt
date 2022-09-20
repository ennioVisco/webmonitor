package at.ac.tuwien.trustcps.tracking

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openqa.selenium.*
import java.net.*
import kotlin.test.*

@Disabled("Needs to be refactored to take into account the new event-based model.")
internal class OldPageTrackerTest {
    private val url = URL("https://google.com")
    private val dims = Dimension(100, 200)

    @Test
    fun `page tracker can track basic page metadata`() {
        val tracker = trackerInit()

        val data = tracker.run()[0]

        assertContains(data, "lvp_height")
        assertContains(data, "lvp_width")
        assertContains(data, "vvp_height")
        assertContains(data, "vvp_width")
        assertEquals(data["lvp_height"], "1")
        assertEquals(data["lvp_width"], "1")
        assertEquals(data["vvp_height"], "1")
        assertEquals(data["vvp_width"], "1")
    }

    @Test
    fun `page tracker can track sample element metadata`() {
        val tracker = trackerInit()

        tracker.select("elem")
        val data = tracker.run()[0]

        assertContains(data, "elem::x")
        assertContains(data, "elem::y")
        assertContains(data, "elem::width")
        assertContains(data, "elem::height")
        assertEquals(data["elem::x"], "1")
        assertEquals(data["elem::y"], "1")
        assertEquals(data["elem::width"], "1")
        assertEquals(data["elem::height"], "1")
    }

    private fun trackerInit(): PageTracker {
        val rectangle = Rectangle(1, 1, 1, 1)
        val elem = mockk<WebElement>()
        val sessionMock = mockk<SessionBuilder>()
        every { sessionMock.driver.executeScript(any<String>()) } returns "1"
        every { sessionMock.driver.findElement(any()) } returns elem
        every { elem.rect } returns rectangle
        justRun { sessionMock.close() }
        val tracker = spyk(
            PageTracker(url, dims, Browser.CHROME),
            recordPrivateCalls = true
        )
        every {
            tracker invoke "spawnBrowserSession" withArguments
                    listOf()
        } returns sessionMock

        return tracker
    }
}
