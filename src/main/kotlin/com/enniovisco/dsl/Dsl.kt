package com.enniovisco.dsl

import com.enniovisco.Spec
import com.enniovisco.WebSource
import com.enniovisco.main


/**
 * Enum of the supported browsers.
 */
typealias Browser = com.enniovisco.tracking.Browser

class WebMonitor {
    fun webSource(init: WebSource.() -> Unit): WebSource {
        WebSource.init()
        return WebSource
    }

    fun spec(init: Spec.() -> Unit): Spec {
        Spec.init()
        return Spec
    }
}

fun monitor(init: WebMonitor.()-> Unit) {
    val webmonitor = WebMonitor()
    webmonitor.init()
    println("Starting WebMonitor...")
    main(emptyArray(), preloaded = true)
}
