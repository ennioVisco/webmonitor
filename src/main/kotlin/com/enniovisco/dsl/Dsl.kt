package com.enniovisco.dsl

import com.enniovisco.Spec
import com.enniovisco.WebSource
import com.enniovisco.main

class WebMonitor {
    var toFile = true
    var toConsole = false

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
    main(emptyArray(), preloaded = true, toFile = webmonitor.toFile, toConsole = webmonitor.toConsole)
}
