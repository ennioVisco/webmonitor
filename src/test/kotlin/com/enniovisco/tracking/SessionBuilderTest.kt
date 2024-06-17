package com.enniovisco.tracking

import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import java.net.URI


internal class SessionBuilderTest {

//    @Disabled("Broken") // TODO: Restore test
    @Test
    fun `browser session is initialized correctly`() {
        val url = URI.create("https://google.com").toURL()
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
}
