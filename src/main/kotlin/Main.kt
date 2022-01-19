
import com.tylerthrailkill.helpers.prettyprint.pp
import mu.KotlinLogging
import org.openqa.selenium.By
import java.net.URL

private val logger = KotlinLogging.logger {}

fun main() {
    val baseUrl = URL("https://www.tuwien.ac.at/")

   val tracker = PageTracker(baseUrl)
    tracker.select("h1")
    tracker.select("h2")

    val data = tracker.track()
    data.pp()
}

fun main_old() {
    val baseUrl = URL("https://www.tuwien.ac.at/")

    SessionBuilder(baseUrl).use {
        // get the actual value of the title
        val actualTitle = it.driver.title
        val expectedTitle = "Welcome: Mercury Tours"

        val wnd = it.driver.manage().window()
//        wnd.setSize(Dimension(800, 600));
        logger.info("Window Size: ${wnd.size}")
        logger.info("Window Position: ${wnd.position}")

        val h1 = it.driver.findElement(By.cssSelector("h1"))
        val res = h1.rect
        // Rectangle class provides getX,getY, getWidth, getHeight methods
        res.pp()

        //it.driver.devTools.createSession();
        val viewport = it.driver.executeScript("return [window.innerWidth, window.innerHeight];")
        logger.info("Viewport $viewport")

        val h12 = it.driver.executeScript("return window.getComputedStyle(document.querySelector('h1')).getPropertyValue('font-size');")
        logger.info("<h1>'s font-size: $h12")

//        it.driver.findElements(By.xpath("//*")).forEach { elem ->
//            elem.rect.pp()
//        }

        if (actualTitle!!.contentEquals(expectedTitle)) {
            logger.info("Test Passed!")
        } else {
            logger.info("Test Failed")
        }
    }
}