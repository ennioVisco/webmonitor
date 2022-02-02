package at.ac.tuwien.trustcps.tracking

import com.tylerthrailkill.helpers.prettyprint.pp
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import java.net.URL
import kotlin.test.assertContains

internal class PageTrackerTest {
    private val url = URL("https://google.com")
    private val dims = Dimension(100, 200)

    @Test fun test() {
        val sessionMock = mockk<SessionBuilder>()
        every { sessionMock.driver.executeScript(any<String>()) } returns "1"
        justRun { sessionMock.close() }

        val tracker = spyk(PageTracker(url, dims, Browser.CHROME),
                           recordPrivateCalls = true)
        every { tracker invoke "spawnBrowserSession" withArguments
                listOf() } returns sessionMock

        val data = tracker.track()

        assertContains(data, "wnd_height")
        assertContains(data, "wnd_width")
        assertContains(data, "vp_height")
        assertContains(data, "vp_width")
        assertEquals(data["wnd_height"], "1")
        assertEquals(data["wnd_width"], "1")
        assertEquals(data["vp_height"], "1")
        assertEquals(data["vp_width"], "1")
    }
}