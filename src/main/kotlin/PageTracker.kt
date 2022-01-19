import mu.KotlinLogging
import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class PageTracker(private val targetPage: URL,
                  private val dimension: Dimension? = null)
{
    private val logger = KotlinLogging.logger {}
    val data = HashMap<String, String>()
    private val selectors = ArrayList<String>()

    /**
     *
     */
    fun track(): Map<String, String> {
        SessionBuilder(targetPage).use {
            val wnd = it.driver.manage().window()

            if(dimension != null)
                wnd.size = dimension

            logger.info("Window Size: ${wnd.size}")
            logger.info("Window Position: ${wnd.position}")

            val wpWidth = it.driver.executeScript("return window.innerWidth;")
            val wpHeight = it.driver.executeScript("return window.innerHeight;")
            data["wnd_width"] = wpWidth.toString()
            data["wnd_height"] = wpHeight.toString()
            logger.info("Viewport: ${wpWidth}x${wpHeight}")

            for(selector in selectors) {
                doSelect(selector, it.driver)
            }
        }
        return data
    }

    private fun doSelect(queryString: String, driver: RemoteWebDriver) {
        val elem = driver.findElement(By.cssSelector(queryString))

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        data["${queryString}::x"] = elem.rect.x.toString()
        data["${queryString}::y"] = elem.rect.y.toString()
        data["${queryString}::width"] = elem.rect.width.toString()
        data["${queryString}::height"] = elem.rect.height.toString()
    }

    /**
     *
     */
    fun select(queryString: String) {
        selectors.add(queryString)
    }

    private fun selectAll(driver: RemoteWebDriver) {
        driver.findElements(By.xpath("//*")).forEach { elem ->
            logger.debug(elem.rect.toString())
        }
    }

    private fun execScript(driver: RemoteWebDriver) {
        //val viewport = driver.executeScript("return [window.innerWidth, window.innerHeight];")
        val h12 = driver.executeScript(
            "return window.getComputedStyle(document.querySelector('h1')).getPropertyValue('font-size');")
        logger.info("<h1>'s font-size: $h12")
    }

}