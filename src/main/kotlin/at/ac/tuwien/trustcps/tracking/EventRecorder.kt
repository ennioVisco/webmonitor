package at.ac.tuwien.trustcps.tracking

import org.openqa.selenium.remote.*

/**
 * Records browser events in the browser's console.
 * @param driver the browser's driver
 * @param prefix the prefix to use for the console messages
 */
class EventRecorder(
    private val driver: RemoteWebDriver,
    private val prefix: String
) {

    /**
     * Records a browser event.
     * @param selector the CSS selector of the element to record the event for
     * @param event the event to record
     */
    fun captureEvent(
        selector: String,
        event: String
    ) {
        exec(// language=JavaScript
            """
                $selector.addEventListener('$event', () => { 
                    ${print("$selector@$event")}  
             });
             """
        )
    }

    /**
     * Records the page-load event.
     */
    fun capturePageLoaded(): Map<String, String> {
        exec(// language=JavaScript
            """
            if(document.readyState === 'complete') {
                ${print("page is fully loaded")}
            } else {
                window.addEventListener('load', () => { 
                    ${print("page is fully loaded")}
                })
            } 
            """
        )

        val getPixelRatio = "return window.devicePixelRatio"
        val currentPixelRatio = driver.executeScript(getPixelRatio).toString()
        return mapOf(
            "devicePixelRatio" to currentPixelRatio,
        )
    }

    private fun print(msg: String) = "console.log('$prefix$msg');"

    private fun exec(command: String) = driver.executeScript(command)
    fun captureTimedEvent(milliseconds: Int) {
        exec(// language=JavaScript
            """
                setInterval(function() {
                ${print("Another ${milliseconds}ms have passed")}
                }, $milliseconds);
             """
        )
    }

}
