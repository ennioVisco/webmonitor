package at.ac.tuwien.trustcps.tracking

import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.Closeable
import java.net.URL
import java.nio.file.Path

class SessionBuilder(url: URL,
                     dims: Dimension? = null,
                     engine: Browser = Browser.CHROME)
    : Closeable
{
    private val driversDir = Path.of("libs").toAbsolutePath()
    private val chromeDriver = "chromedriver_97.exe"
    private val firefoxDriver = "geckodriver.exe"

    var driver: RemoteWebDriver = when (engine) {
            Browser.CHROME -> run {
                System.setProperty(
                    "webdriver.chrome.driver",
                    "$driversDir\\$chromeDriver")
                ChromeDriver()
            }
            Browser.FIREFOX -> run {
                System.setProperty(
                    "webdriver.gecko.driver",
                    "$driversDir\\$firefoxDriver")
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