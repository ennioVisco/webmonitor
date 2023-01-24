package at.ac.tuwien.trustcps.tracking

import io.mockk.*
import org.junit.jupiter.api.Test
import org.openqa.selenium.*
import org.openqa.selenium.remote.*
import kotlin.test.*

internal class SnapshotBuilderTest {
    private val selectors = listOf("body")
    private val firstAndOnlyOne = 0

    @Test
    fun `can fetch a basic selector`() {
        val fakeDriver = mockWebDrive()
        every { fakeDriver.findElement(any()) } returns mockWebElement()
        every { fakeDriver.getScreenshotAs(OutputType.FILE) } returns mockk()

        val snapshot = SnapshotBuilder(fakeDriver, selectors, emptyList())

        assertEquals("0", snapshot.collect(firstAndOnlyOne)["body::y"])
    }

    private fun mockWebDrive(): RemoteWebDriver {
        val driver = mockk<RemoteWebDriver>()
        every { driver.executeScript(any<String>()) } returns "body"
        return driver
    }

    private fun mockWebElement(): WebElement {
        val element = mockk<WebElement>()
        every { element.rect } returns Rectangle(0, 0, 100, 100)
        return element
    }
}
