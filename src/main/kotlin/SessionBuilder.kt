import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.Closeable
import java.net.URL
import java.nio.file.Path

class SessionBuilder(url: URL, engine: String = "chrome") : Closeable {
    private val driversDir = Path.of("libs").toAbsolutePath()
    private val chromeDriver = "chromedriver_97.exe"
    private val firefoxDriver = "geckodriver.exe"

    var driver: RemoteWebDriver = when (engine) {
            "chrome" -> run {
                System.setProperty(
                    "webdriver.chrome.driver",
                    "$driversDir\\$chromeDriver")
                ChromeDriver()
            }
            "firefox" -> run {
                System.setProperty(
                    "webdriver.gecko.driver",
                    "$driversDir\\$firefoxDriver")
                FirefoxDriver()
            }
            else -> throw IllegalArgumentException("Invalid driver selected!")
        }

    init {
        driver.get(url.toString())
    }

    override fun close() {
        //close all webdriver sessions
        driver.quit()
    }
}