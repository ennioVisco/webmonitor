import at.ac.tuwien.trustcps.dsl.*

Spec.atoms(
    select { ".cookieInfo" }  // [0]
            read "visibility"
            equals "visible",
    select { ".subtitle" },   // [1]
    select { "button#close" } // [2]
)

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
Spec.formula = er1 and er2
