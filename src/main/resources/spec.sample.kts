import at.ac.tuwien.trustcps.dsl.*

Spec.atoms(
    select { ".cookieInfo" }    // [0]
            read "visibility"
            equalTo "visible",
    select { ".subtitle" },     // [1]
    select { "button#close" },  // [2]
    select { ".cookieInfo" } read "position" bind "pos",
    select { ".cookieInfo" } read "font-size" bind "fs",
    select { ".info" } read "position" bind "pos",
    select { ".subtitle" } read "font-size" bind "fs"
            applying { value, bound -> parsePixels(value) == parsePixels(bound) * 1.5 },
    select { "h2.subtitle" },
    select { "p" }
)

fun parsePixels(value: String): Double {
    return value.substring(0, value.length - 2).toDouble()
}

Spec.record(
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
Spec.formula = Spec.atoms[6] and not(Spec.atoms[8])
