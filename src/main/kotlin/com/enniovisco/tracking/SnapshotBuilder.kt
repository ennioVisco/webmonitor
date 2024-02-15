package com.enniovisco.tracking

import com.enniovisco.tracking.commands.*
import io.github.oshai.kotlinlogging.*
import org.apache.commons.io.*
import org.openqa.selenium.*
import org.openqa.selenium.devtools.*
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
    private val log = KotlinLogging.logger {}

    /**
     * Fetches the snapshot data from the current session of the browser
     * @param id identifier of the snapshot (used for storing the screenshot)
     * @return a map of SnapshotData pairs
     */
    fun collect(id: Int): Map<String, String> {
        val data = HashMap<String, String>()
        fetchMetadata(data)
        selectors.forEach { doSelect(it, data) }
        if (toFile) takeScreenshot(id)
        return data
    }

    private fun fetchMetadata(data: MutableMap<String, String>) =
        MetadataCollector(driver::executeScript).dump(data)

    private fun doSelect(cssQuery: String, data: MutableMap<String, String>) {
        try {
            findAndDump(cssQuery, data)
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException,
                is DevToolsException -> {
                    log.error {
                        "Selector '$cssQuery' not found. " +
                                "Are you sure it is present in the page?"
                    }
                    throw e
                }

                else -> {
                    log.warn { "Unable to find data for selector '$cssQuery'." }
//                    throw e
                }
            }
        }
    }

    private fun findAndDump(
        cssQuery: String,
        data: MutableMap<String, String>
    ) {
        val findCss = { q: String -> driver.findElements(By.cssSelector(q)) }
        val elem = SelectorCollector(cssQuery, findCss, driver::executeScript)
        elem.dump(data)
    }


    private fun takeScreenshot(id: Int) {
        val screenshotFile = driver.getScreenshotAs(OutputType.FILE)
        val outputFile = File("./output/snap_${id}.png")
        FileUtils.copyFile(screenshotFile, outputFile)
    }
}
