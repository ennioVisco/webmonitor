package at.ac.tuwien.trustcps.tracking

import at.ac.tuwien.trustcps.dsl.*
import org.apache.commons.io.*
import org.openqa.selenium.*
import org.openqa.selenium.remote.*
import java.io.*

/**
 * Fetches the snapshot data from the current session of the browser
 *
 * @property driver Browser session to interact with
 * @property selectors list of HTML selectors of interest in the snapshot
 * @property toFile option to tell whether a screenshot must be saved as a file or not
 */
class SnapshotBuilder(
    private val driver: RemoteWebDriver,
    private val selectors: List<String>,
    private val toFile: Boolean = false
) {

    /**
     * Fetches the snapshot data from the current session of the browser
     * @param id identifier of the snapshot (used for storing the screenshot)
     * @return a map of SnapshotData pairs
     */
    fun collect(id: Int): Map<String, String> {
        val data = HashMap<String, String>()
        fetchMetadata(data)
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
        data: HashMap<String, String>
    ) {
        // Document: areas considered by the page, including unreachable ones.
        // They are not tracked at all at the moment
        //
        // Layout Viewport: Scrollable area
        val layoutVp = "return document.documentElement"
        val layoutVpWidth = exec("${layoutVp}.scrollWidth")
        val layoutVpHeight = exec("${layoutVp}.scrollHeight")
        data["lvp_width"] = layoutVpWidth.toString()
        data["lvp_height"] = layoutVpHeight.toString()
        println("Layout Viewport: ${layoutVpWidth}x${layoutVpHeight}")

        // Visual Viewport: physical screen
        val visualVpWidth = exec("return window.innerWidth;")
        val visualVpHeight = exec("return window.innerHeight;")
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

    private fun exec(command: String) = driver.executeScript(command)
}
