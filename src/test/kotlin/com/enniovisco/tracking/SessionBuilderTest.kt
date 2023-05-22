package com.enniovisco.tracking

import io.github.bonigarcia.wdm.*
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.openqa.selenium.*
import org.openqa.selenium.chrome.*
import java.net.*


internal class SessionBuilderTest {

    @Test
    fun `browser session is initialized correctly`() {
        val url = URL("https://google.com")
        val dims = Dimension(800, 600)

        try {
            SessionBuilder(url, mockk(), dims).use {

                assertInstanceOf(ChromeDriver::class.java, it.driver)
                assertEquals(dims, it.driver.manage().window().size)
            }
        } catch (e: Exception) {
            fail("Are you sure the Chrome browser is installed?", e)
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
