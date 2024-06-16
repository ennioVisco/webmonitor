#!/usr/bin/env kotlin

@file:DependsOn("com.enniovisco:webmonitor:v1.2.0-beta.2")
//@file:DependsOn("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")

//import kotlinx.html.*; import kotlinx.html.stream.*; import kotlinx.html.attributes.*

import com.enniovisco.*

webSource {
    screenWidth = 100
}

val addressee = "World"

//print(
//    createHTML().html {
//        body {
//            h1 { +"Hello, $addressee!" }
//        }
//    }
//)