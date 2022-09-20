package at.ac.tuwien.trustcps.tracking

import io.github.bonigarcia.wdm.*
import org.openqa.selenium.*
import org.openqa.selenium.chrome.*
import org.openqa.selenium.devtools.*
import org.openqa.selenium.devtools.events.*
import org.openqa.selenium.firefox.*
import org.openqa.selenium.remote.*
import java.io.*
import java.net.*

/**
 * Generates a WebDriver instance for the given browser (engine).
 * @param url the url the session should be started with
 * @param eventsHandler the handler for the events recorded on the page
 * @param dims the dimensions of the browser window
 * @param engine the browser engine to use (optional)
 */
class SessionBuilder(
    url: URL,
    eventsHandler: (ConsoleEvent) -> Unit,
    dims: Dimension? = Dimension(1920, 1080),
    engine: Browser = Browser.CHROME_HEADLESS
) : Closeable {
    /**
     * The WebDriver instance.
     */
    val driver: RemoteWebDriver = run {
        val browserDriver = when (engine) {
            Browser.CHROME -> initChromeSettings(dims, false)
            Browser.CHROME_HEADLESS -> initChromeSettings(dims, true)
            Browser.FIREFOX -> initFirefoxSettings()
        }
        startDevTools(browserDriver, eventsHandler)
        browserDriver
    }

    private fun initFirefoxSettings(): FirefoxDriver {
        WebDriverManager.firefoxdriver().setup()
        return FirefoxDriver()
    }

    private fun initChromeSettings(
        dims: Dimension?,
        headless: Boolean
    ): ChromeDriver {
        WebDriverManager.chromedriver().setup()
        val options = ChromeOptions()
        if (dims?.width!! < 500 || dims.height < 400) {
            //val wdm = WebDriverManager.chromedriver().browserInDockerAndroid()
            //return wdm.create() as ChromeDriver
            val mobileEmulation = mapOf("deviceName" to "iPhone 5/SE")
            options.setExperimentalOption(
                "mobileEmulation",
                mobileEmulation
            )
        }

        if (headless) {
            options.setHeadless(true)
        }

        options.addArguments("--force-device-scale-factor=1")
        return ChromeDriver(options)
    }

    private fun startDevTools(
        driver: HasDevTools,
        handler: (ConsoleEvent) -> Unit
    ) {
        val devTools = driver.devTools
        devTools.createSession()
        devTools.domains.events().addConsoleListener(handler)
    }

    init {
        driver.manage().window().size = dims
        driver.get(url.toString())
    }

    override fun close() {
        driver.quit() // close all webdriver sessions
    }
}
