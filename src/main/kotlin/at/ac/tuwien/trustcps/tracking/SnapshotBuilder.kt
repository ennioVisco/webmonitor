package at.ac.tuwien.trustcps.tracking

import at.ac.tuwien.trustcps.parsing.parseSelector
import org.apache.commons.io.FileUtils
import org.openqa.selenium.*
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.File

class SnapshotBuilder(
    private val driver: RemoteWebDriver,
    private val selectors: List<String>,
    private val toFile: Boolean = false
) {
    fun collect(id: Int): Map<String, String> {
        val data = HashMap<String, String>()
        fetchMetadata(driver, data)
        for (selector in selectors) {
            doSelect(selector, driver, data)
        }
        takeScreenshot(id)
        return data
    }

    private fun takeScreenshot(id: Int) {
        if (toFile) {
            val scrFile = driver.getScreenshotAs(OutputType.FILE)
            val outputFile = File("./output/snap_${id}.png")
            FileUtils.copyFile(scrFile, outputFile)
        }
    }

    private fun fetchMetadata(
        driver: RemoteWebDriver,
        data: HashMap<String, String>
    ) {
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

    private fun doSelect(
        queryString: String,
        driver: RemoteWebDriver,
        data: HashMap<String, String>
    ) {
        val (cssQuery, cssProperty, _) = parseSelector(queryString)
        val elem = driver.findElement(By.cssSelector(cssQuery))

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        data["${cssQuery}::x"] = elem.rect.x.toString()
        data["${cssQuery}::y"] = elem.rect.y.toString()
        data["${cssQuery}::width"] = elem.rect.width.toString()
        data["${cssQuery}::height"] = elem.rect.height.toString()

        println(
            "Element <${cssQuery}> = " +
                    "(${data["${cssQuery}::x"]}, ${data["${cssQuery}::y"]})" +
                    " -> " +
                    "(${data["${cssQuery}::width"]}, ${data["${cssQuery}::height"]})"
        )

        if (cssProperty != "") {
            data["${cssQuery}::${cssProperty}"] = elem.getCssValue(cssProperty)
            println("property value: ${data["${cssQuery}::${cssProperty}"]}")
        }
    }


    private fun exec(driver: RemoteWebDriver, command: String): Any? {
        return driver.executeScript(command)
    }
}
