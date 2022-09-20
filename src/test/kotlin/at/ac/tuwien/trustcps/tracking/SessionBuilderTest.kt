package at.ac.tuwien.trustcps.tracking

import io.github.bonigarcia.wdm.*
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.openqa.selenium.*
import org.openqa.selenium.chrome.*
import java.net.*


internal class SessionBuilderTest {
    //@Disabled("Needs way to mock driver execution")
    @Test
    fun `browser session is initialized correctly`() {
        val url = URL("https://google.com")
        val dims = Dimension(800, 600)

        SessionBuilder(url, mockk(), dims).use {

            assertInstanceOf(ChromeDriver::class.java, it.driver)
            assertEquals(dims, it.driver.manage().window().size)
        }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupClass() {
            WebDriverManager.chromedriver().setup()
        }
    }
}
