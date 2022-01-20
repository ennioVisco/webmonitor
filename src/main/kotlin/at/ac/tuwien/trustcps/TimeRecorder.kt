package at.ac.tuwien.trustcps

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.devtools.events.CdpEventTypes.domMutation
import org.openqa.selenium.devtools.events.DomMutationEvent
import java.nio.file.Path
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val DRIVERS_DIR = Path.of("libs").toAbsolutePath()
private const val CHROME_DRIVER = "chromedriver_96.exe"




fun main() {
    System.setProperty(
        "webdriver.chrome.driver",
        "$DRIVERS_DIR\\$CHROME_DRIVER")

//    at.ac.tuwien.trustcps.tracker.SessionBuilder("https://www.google.com").use {
//        val seen = AtomicReference<DomMutationEvent>()
//        val latch = CountDownLatch(1)
//        (it.driver as ChromeDriver).onLogEvent(domMutation{ mutation ->
//            seen.set(mutation)
//            latch.countDown()
//        })
//
//        val span = it.driver.findElement (By.cssSelector("span"))
//
//        it.driver.executeScript("arguments[0].setAttribute('cheese', 'gouda');", span)
//
//        assertTrue(latch.await(10, SECONDS))
//        assertEquals(seen.get().attributeName, "cheese")
//        assertEquals(seen.get().currentValue, "gouda")
//
//    }

    val driver = ChromeDriver()

    val seen = AtomicReference<DomMutationEvent>()
    val latch = CountDownLatch(10)
    driver.onLogEvent(domMutation{ mutation ->
        seen.set(mutation)
        latch.countDown()
    })

    driver.get("https://www.google.com")
    val span = driver . findElement (By.cssSelector("span"))

    driver.executeScript("arguments[0].setAttribute('cheese', 'gouda');", span)

    try {
        assertTrue(latch.await(10, SECONDS))
        assertEquals(seen.get().attributeName, "cheese")
        assertEquals(seen.get().currentValue, "gouda")
        println("Assertions succeeded")
    } catch(e: Exception) {
        println("Assertions failed")
    }

    driver.quit()
}