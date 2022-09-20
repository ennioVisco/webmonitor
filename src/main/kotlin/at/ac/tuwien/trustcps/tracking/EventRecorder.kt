package at.ac.tuwien.trustcps.tracking

import org.openqa.selenium.remote.*

class EventRecorder(
    private val driver: RemoteWebDriver,
    private val prefix: String
) {
    init {
        exec("window.devicePixelRatio = 1;")
    }

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

    fun capturePageLoaded() {
        exec(// language=JavaScript
            """
            if(document.readyState === "complete") {
                ${print("page is fully loaded")}
            } else {
                window.addEventListener('load', () => { 
                    ${print("page is fully loaded")}
                })
            } 
            """
        )
    }

    private fun print(msg: String) = "console.log('$prefix$msg');"

    private fun exec(command: String) = driver.executeScript(command)

}
