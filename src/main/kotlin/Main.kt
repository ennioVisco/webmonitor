
import com.tylerthrailkill.helpers.prettyprint.pp
import org.openqa.selenium.By

fun main() {
    val baseUrl = "https://www.tuwien.ac.at/"

    SessionBuilder(baseUrl).use {
        // get the actual value of the title
        val actualTitle = it.driver.title
        val expectedTitle = "Welcome: Mercury Tours"

        val wnd = it.driver.manage().window()
//        wnd.setSize(Dimension(800, 600));
        println("Window Size: ${wnd.size}")
        println("Window Position: ${wnd.position}")

        val h1 = it.driver.findElement(By.cssSelector("h1"))
        val res = h1.rect
        // Rectangle class provides getX,getY, getWidth, getHeight methods
        res.pp()

        //it.driver.devTools.createSession();
        val viewport = it.driver.executeScript("return [window.innerWidth, window.innerHeight];")
        println("Viewport $viewport")

//        it.driver.findElements(By.xpath("//*")).forEach { elem ->
//            elem.rect.pp()
//        }

        if (actualTitle!!.contentEquals(expectedTitle)){
            println("Test Passed!")
        } else {
            println("Test Failed")
        }
    }
}