package at.ac.tuwien.trustcps.tracking

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import java.net.URL

internal class SessionBuilderTest {
    @Disabled
    @Test fun `browser session is initialized correctly`() {
        val url = URL("https://google.com")
        val dims = Dimension(800, 600)

        SessionBuilder(url, dims, Browser.CHROME).use {

            assertInstanceOf(ChromeDriver::class.java, it.driver)
            assertEquals(dims, it.driver.manage().window().size)
        }
    }
}