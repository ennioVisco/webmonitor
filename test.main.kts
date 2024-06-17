#!/usr/bin/env kotlin

@file:DependsOn("com.enniovisco:webmonitor:v1.2.0-beta.7")

import com.enniovisco.dsl.*

monitor {
    webSource {
        screenWidth = 393 // px
        screenHeight = 851 // px
        // To date Google Chrome is the browser with the most stable APIs
        browser = Browser.CHROME_HEADLESS
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

//println(WebSource.screenWidth)

//print(
//    createHTML().html {
//        body {
//            h1 { +"Hello, $addressee!" }
//        }
//    }
//)