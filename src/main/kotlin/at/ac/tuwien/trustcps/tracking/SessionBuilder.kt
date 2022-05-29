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


class SessionBuilder(
    url: URL,
    eventsHandler: (ConsoleEvent) -> Unit,
    dims: Dimension? = null,
    engine: Browser = Browser.CHROME
) : Closeable {
    val driver: RemoteWebDriver = run {
        val browserDriver = when (engine) {
            Browser.CHROME -> initChromeSettings(dims)
            Browser.FIREFOX -> initFirefoxSettings()
        }
        startDevTools(browserDriver, eventsHandler)
        browserDriver
    }

    private fun initFirefoxSettings(): FirefoxDriver {
        WebDriverManager.firefoxdriver().setup()
        return FirefoxDriver()
    }

    private fun initChromeSettings(dims: Dimension?): ChromeDriver {
        WebDriverManager.chromedriver().setup()
        return if (dims?.width!! < 500 || dims.height < 400) {
            //            val wdm = WebDriverManager.chromedriver().browserInDockerAndroid()
            //            return wdm.create() as ChromeDriver
            val mobileEmulation: MutableMap<String, String> = HashMap()
            mobileEmulation["deviceName"] = "iPhone 5/SE"
            val chromeOptions = ChromeOptions()
            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation)
            ChromeDriver(chromeOptions)
        } else {
            ChromeDriver()
        }
    }

    private fun startDevTools(driver: HasDevTools, handler: (ConsoleEvent) -> Unit) {
        val devTools = driver.devTools
        devTools.createSession()
        devTools.domains.events().addConsoleListener(handler)
    }

    init {
        driver.manage().window().size = dims ?: Dimension(1920, 1080)
        driver.get(url.toString())
    }

    override fun close() {
        driver.quit() // close all webdriver sessions
    }
}
