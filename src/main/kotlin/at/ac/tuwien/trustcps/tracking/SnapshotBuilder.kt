package at.ac.tuwien.trustcps.tracking

import org.apache.commons.io.FileUtils
import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File

class SnapshotBuilder(
    private val driver: RemoteWebDriver,
    private val selectors: List<String>,
    private val id: Int,
    private val toFile: Boolean = false
) {
    fun collect(): Map<String, String> {
        val data = HashMap<String, String>()
        fetchMetadata(driver, data)
        for (selector in selectors) {
            doSelect(selector, driver, data)
        }
        takeScreenshot()
        return data
    }

    private fun takeScreenshot() {
        if (toFile) {
            val screenshot = driver as TakesScreenshot
            val scrFile = screenshot.getScreenshotAs(OutputType.FILE)
            FileUtils.copyFile(scrFile, File("./output/snap_${id}.png"))
        }
    }

    private fun fetchMetadata(driver: RemoteWebDriver, data: HashMap<String, String>) {
        // Document: areas considered by the page, including unreachable ones.
        // They are not tracked at all at the moment
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


    }

    private fun doSelect(queryString: String, driver: RemoteWebDriver, data: HashMap<String, String>) {
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
}
