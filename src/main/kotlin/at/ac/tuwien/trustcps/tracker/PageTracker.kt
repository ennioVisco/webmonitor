package at.ac.tuwien.trustcps.tracker

import org.openqa.selenium.By
import org.openqa.selenium.Dimension
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class PageTracker(private val targetPage: URL,
                  private val dimension: Dimension? = null,
                  private val browser: Browser? = null)
{
    val data = HashMap<String, String>()
    private val selectors = ArrayList<String>()

    /**
     *
     */
    fun track(): Map<String, String> {
        SessionBuilder(targetPage, dimension, browser ?: Browser.CHROME)
            .use {
            val wnd = it.driver.manage().window()
//
//            if(dimension != null)
//                wnd.size = dimension

            println("Window Size: ${wnd.size}")
            println("Window Position: ${wnd.position}")

            // Viewport
            val vpWidth = it.driver.executeScript("return window.innerWidth;")
            val vpHeight = it.driver.executeScript("return window.innerHeight;")
            data["vp_width"] = vpWidth.toString()
            data["vp_height"] = vpHeight.toString()
            println("Viewport: ${vpWidth}x${vpHeight}")

            // Browser frame
            val wndWidth = it.driver.executeScript("return screen.availWidth;")
            val wndHeight = it.driver.executeScript("return screen.availHeight;")
            data["wnd_width"] = wndWidth.toString()
            data["wnd_height"] = wndHeight.toString()
            println("Window: ${wndWidth}x${vpHeight}")

            Thread.sleep(5_000)

            for(selector in selectors) {
                doSelect(selector, it.driver)
            }

            //Thread.sleep(5_000)
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
//        val rect = driver.executeScript(
//            "return document.querySelector('${queryString}').getBoundingClientRect()")
//        data["${queryString}::rect"] = rect.toString()
    }

    /**
     *
     */
    fun select(queryString: String) {
        selectors.add(queryString)
    }

    fun selectAll(driver: RemoteWebDriver) {
        driver.findElements(By.xpath("//*")).forEach { elem ->
            println(elem.rect.toString())
        }
    }

    fun execScript(driver: RemoteWebDriver) {
        //val viewport = driver.executeScript("return [window.innerWidth, window.innerHeight];")
        val h12 = driver.executeScript(
            "return window.getComputedStyle(document.querySelector('h1')).getPropertyValue('font-size');")
        println("<h1>'s font-size: $h12")
    }

}