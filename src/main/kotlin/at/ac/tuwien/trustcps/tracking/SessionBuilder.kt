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
    private val chromeVerticalBrowserFrame = 133 // Chrome browser's offset

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
        options.addArguments("--remote-allow-origins=*")
        if (dims?.width!! < 500 || dims.height < 400) {
            initMobileChrome(options, "iPhone 5/SE")
        } else {
            val windowSizeFlag = "--window-size=${dims.width},${dims.height}"
            options.addArguments(windowSizeFlag)
        }

        if (headless) {
            options.setHeadless(true)
            options.addArguments("--no-sandbox")
            options.addArguments("--disable-dev-shm-usage")
        }

        return ChromeDriver(options)
    }

    private fun initMobileChrome(options: ChromeOptions, device: String) {
        val mobileDevice = selectMobileDevice(device)
        val mobileEmulation = mobileDevice.deviceName
        options.setExperimentalOption("mobileEmulation", mobileEmulation)
        val windowWidth = mobileDevice.width
        val windowHeight = mobileDevice.height + chromeVerticalBrowserFrame
        val windowSizeFlag = "--window-size=$windowWidth,$windowHeight"
        options.addArguments(windowSizeFlag)
    }

    private fun selectMobileDevice(key: String): Device {
        val devices: List<Device> = listOf(
            Device("iPhone 5/SE", 375, 667),
        )
        return devices.find { it.name == key }
            ?: throw Error("The device \"$key\" is not supported yet!")
    }

    private data class Device(
        val name: String,
        val width: Int,
        val height: Int
    ) {
        val deviceName = mapOf("deviceName" to name)
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
//        driver.manage().window().size = dims
        driver.get(url.toString())
    }

    override fun close() {
        driver.quit() // close all webdriver sessions
    }
}
