package at.ac.tuwien.trustcps.tracking

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.HasDevTools
import org.openqa.selenium.devtools.events.ConsoleEvent
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.io.Closeable
import java.net.URL


class SessionBuilder(
    url: URL,
    eventsHandler: (ConsoleEvent) -> Unit,
    dims: Dimension? = null,
    engine: Browser? = Browser.CHROME
) : Closeable {
    val driver: RemoteWebDriver = when (engine) {
        Browser.CHROME -> run {

            val driver = initChromeSettings(dims)
            startDevTools(driver, eventsHandler)
            driver
        }
        Browser.FIREFOX -> run {
            WebDriverManager.firefoxdriver().setup()
            val driver = FirefoxDriver()
            startDevTools(driver, eventsHandler)
            driver
        }
        else -> error("The provided browser is not supported")
    }

    private fun initChromeSettings(dims: Dimension?): ChromeDriver {
        if (dims?.width!! < 500 || dims.height < 400) {
//            val wdm = WebDriverManager.chromedriver().browserInDockerAndroid()
//            return wdm.create() as ChromeDriver
            WebDriverManager.chromedriver().setup()
            val mobileEmulation: MutableMap<String, String> = HashMap()
            mobileEmulation["deviceName"] = "iPhone 5/SE"
            val chromeOptions = ChromeOptions()
            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation)
            return ChromeDriver(chromeOptions)

        } else {
            WebDriverManager.chromedriver().setup()
            return ChromeDriver()
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
        //close all webdriver sessions
        driver.quit()
    }

//    private fun something() {
//        val mobileEmulation = mapOf(
//            "deviceName" to "Pixel 5"
//        )
//
//        val chromeOptions = ChromeOptions()
//
//        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation)
//
//        val driver = ChromeDriver(chromeOptions)
//    }
}