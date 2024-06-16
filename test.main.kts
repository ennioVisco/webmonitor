#!/usr/bin/env kotlin

@file:DependsOn("com.enniovisco:webmonitor:v1.2.0-beta.3")

import com.enniovisco.*
import com.enniovisco.dsl.*

webSource {
    screenWidth = 100
}

val addressee = "World"

println(WebSource.screenWidth)

//print(
//    createHTML().html {
//        body {
//            h1 { +"Hello, $addressee!" }
//        }
//    }
//)