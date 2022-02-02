package at.ac.tuwien.trustcps.tracking

import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.Closeable
import java.net.URL
import io.github.bonigarcia.wdm.WebDriverManager

class SessionBuilder(url: URL,
                     dims: Dimension? = null,
                     engine: Browser = Browser.CHROME)
    : Closeable {
    val driver: RemoteWebDriver = when (engine) {
            Browser.CHROME -> run {
                WebDriverManager.chromedriver().setup()
                ChromeDriver()
            }
            Browser.FIREFOX -> run {
                WebDriverManager.firefoxdriver().setup()
                FirefoxDriver()
            }
        }

    init {
        driver.manage().window().size = dims?: Dimension(1920, 1080)
        driver.get(url.toString())
    }

    override fun close() {
        //close all webdriver sessions
        driver.quit()
    }
}