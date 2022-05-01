package at.ac.tuwien.trustcps.tracking

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
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
            initChromeSettings(dims)
            startChromeDriver(eventsHandler)
        }
        Browser.FIREFOX -> run {
            WebDriverManager.firefoxdriver().setup()
            FirefoxDriver()
        }
        else -> unsupportedBrowser()
    }

    private fun initChromeSettings(dims: Dimension?) {
        if (dims?.width!! < 500 || dims.height < 400) {
            WebDriverManager.chromedriver().browserInDockerAndroid().setup()
        } else {
            WebDriverManager.chromedriver().setup()
        }
    }

    private fun startChromeDriver(handler: (ConsoleEvent) -> Unit): RemoteWebDriver {
        val driver = ChromeDriver()
        val devTools = driver.devTools
        devTools.createSession()
        devTools.domains.events().addConsoleListener(handler)
        return driver
    }

    init {
        driver.manage().window().size = dims ?: Dimension(1920, 1080)
        driver.get(url.toString())
    }

    override fun close() {
        //close all webdriver sessions
        driver.quit()
    }

    private fun unsupportedBrowser(): RemoteWebDriver {
        throw UnsupportedOperationException("The provided browser is not supported")
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