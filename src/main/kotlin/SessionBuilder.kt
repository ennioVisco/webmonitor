
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.Closeable

private const val CHROME_DRIVER = "C:\\Users\\Ennio\\source\\repos\\webmonitor\\libs\\chromedriver_96.exe"
private const val FIREFOX_DRIVER = "C:\\Users\\Ennio\\source\\repos\\webmonitor\\libs\\geckodriver.exe"

class SessionBuilder(url: String, engine: String = "chrome") : Closeable {
    var driver: RemoteWebDriver = when (engine) {
            "chrome" -> run {
                System.setProperty("webdriver.chrome.driver", CHROME_DRIVER)
                ChromeDriver()
            }
            "firefox" -> run {
                System.setProperty("webdriver.gecko.driver", FIREFOX_DRIVER)
                FirefoxDriver()
            }
            else -> throw IllegalArgumentException("Invalid driver selected!")
        }

    init {
        driver.get(url)
    }

    override fun close() {
        //close all webdriver sessions
        driver.quit()
    }
}