package at.ac.tuwien.trustcps.tracking

import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File
import java.net.URL


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
    private val toFile: Boolean = false
) {
    private val data = HashMap<String, String>()
    private val selectors = ArrayList<String>()

    /**
     * selects some elements to track from the page
     */
    fun select(queryString: String) {
        selectors.add(queryString)
    }

    /**
     * Tracks the provided selectors for the provided page
     */
    fun track(): Map<String, String> {
        spawnBrowserSession().use {
            fetchMetadata(it.driver)
            //Thread.sleep(200_000)

            for (selector in selectors) {
                doSelect(selector, it.driver)
            }
            //Thread.sleep(5_000)
        }
        return data
    }

    private fun spawnBrowserSession() = SessionBuilder(page, dimension, browser)

    private fun fetchMetadata(driver: RemoteWebDriver) {
        // Document: areas considered by the page, including unreachable ones.
        // They are not tracked at all
        //
        // Layout Viewport: Scrollable area
        val layoutVp = "return document.documentElement"
        val layoutVpWidth = exec(driver, "${layoutVp}.scrollWidth")
        val layoutVpHeight = exec(driver, "${layoutVp}.scrollHeight")
        data["lvp_width"] = layoutVpWidth.toString()
        data["lvp_height"] = layoutVpHeight.toString()
        println("Layout Viewport: ${layoutVpWidth}x${layoutVpHeight}")

        // Visual Viewport: physical screen
        val visualVpWidth = exec(driver, "return window.innerWidth;")
        val visualVpHeight = exec(driver, "return window.innerHeight;")
        data["vvp_width"] = visualVpWidth.toString()
        data["vvp_height"] = visualVpHeight.toString()
        println("Visual Viewport: ${visualVpWidth}x${visualVpHeight}")

        if (toFile) {
            val screenshot = driver as TakesScreenshot
            val scrFile = screenshot.getScreenshotAs(OutputType.FILE)
            FileUtils.copyFile(scrFile, File("./image.png"))
        }

    }

    private fun doSelect(queryString: String, driver: RemoteWebDriver) {
        val elem = driver.findElement(By.cssSelector(queryString))

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        data["${queryString}::x"] = elem.rect.x.toString()
        data["${queryString}::y"] = elem.rect.y.toString()
        data["${queryString}::width"] = elem.rect.width.toString()
        data["${queryString}::height"] = elem.rect.height.toString()
//        val rect = driver.executeScript(
//            "return document.querySelector('${queryString}').getBoundingClientRect()"
//        )
//        data["${queryString}::rect"] = rect.toString()
        println(
            "Element <${queryString}> = " +
                    "(${data["${queryString}::x"]}, ${data["${queryString}::y"]})" +
                    " -> " +
                    "(${data["${queryString}::width"]}, ${data["${queryString}::height"]})"
        )
    }

    private fun exec(driver: RemoteWebDriver, command: String): Any? {
        return driver.executeScript(command)
    }

//    fun selectAll(driver: RemoteWebDriver) {
//        driver.findElements(By.xpath("//*")).forEach { elem ->
//            println(elem.rect.toString())
//        }
//    }
//
//    fun execScript(driver: RemoteWebDriver) {
//        //val viewport = driver.executeScript("return [window.innerWidth, window.innerHeight];")
//        val h12 = driver.executeScript(
//            "return window.getComputedStyle(document.querySelector('h1')).getPropertyValue('font-size');")
//        println("<h1>'s font-size: $h12")
//    }

}