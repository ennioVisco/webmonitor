#!/bin/bash

//usr/bin/env echo '
/**** BOOTSTRAP kscript ****\'>/dev/null
command -v kscript >/dev/null 2>&1 || source /dev/stdin <<< "$(curl -L https://git.io/fpF1K)"
exec kscript $0 "$@"
\*** IMPORTANT: Any code including imports and annotations must come after this line ***/

@file:DependsOn("com.enniovisco:webmonitor:1.3.0")

import com.enniovisco.dsl.*

monitor {
    webSource {
        screenWidth = 393 // px
        screenHeight = 851 // px
        // To date Google Chrome is the browser with the most stable APIs
        browser = Browser.CHROME_HEADLESS // Alternatively Browser.FIREFOX, Browser.EDGE or Browser.CHROME
        wait = 0
        maxSessionDuration = 5_000 // ms
        targetUrl = "https://enniovisco.github.io/webmonitor/sample.html"
    }

    spec {
        atoms(
            select { ".cookieInfo" }    // [0]
                    read "visibility"
                    equalTo "visible",
            select { ".subtitle" },     // [1]
            select { "button#close" },  // [2]
            select { ".cookieInfo" } read "position" bind "pos",
            select { ".cookieInfo" } read "font-size" bind "fs",
            select { ".info" } read "position" bind "pos",
            select { ".subtitle" } read "font-size" bind "fs"
                    applying { value, bound ->
                parsePixels(value) == parsePixels(bound) * 1.5
            },
            select { "h2.subtitle" },
            select { "p" }
        )

        record(
            after { "click" },
            after { "touch" }
        )


        // helper formulae
        val screen = Spec.screen
        val popupIsVisible = Spec.atoms[0]
        val subtitle = Spec.atoms[1]
        val closeButton = Spec.atoms[2]
        val er1 = closeButton implies screen
        val er2 = not(popupIsVisible and subtitle)

        // Final formula
        formula = atoms[6] and not(atoms[8]) // and everywhere(er1)
    }
}