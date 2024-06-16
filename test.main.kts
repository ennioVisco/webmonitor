#!/usr/bin/env kotlin

@file:DependsOn("com.enniovisco:webmonitor:v1.2.0-beta.5")
////@file:DependesOn("org.slf4j:slf4j-simple:2.0.13")
//@file:DependsOn("io.github.oshai:kotlin-logging-jvm:6.0.9")
//@file:DependsOn("org.slf4j:slf4j-api:2.0.7")
//@file:DependsOn("ch.qos.logback:logback-classic:1.5.6")

import com.enniovisco.dsl.*
import com.enniovisco.*

monitor {
    webSource {
        screenWidth = 393 // px
        screenHeight = 851 // px
        // To date Google Chrome is the browser with the most stable APIs
        browser = Browser.FIREFOX
        wait = 0
        maxSessionDuration = 5_000 // ms
        targetUrl = "https://enniovisco.github.io/webmonitor/sample.html"
    }

    spec {
        atoms(
            select { "#css" }
        )

        record(
            after { "click" }
        )

        formula = screen and atoms[0]
    }
}

println(WebSource.screenWidth)

//print(
//    createHTML().html {
//        body {
//            h1 { +"Hello, $addressee!" }
//        }
//    }
//)