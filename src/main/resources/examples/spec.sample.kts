import at.ac.tuwien.trustcps.dsl.*

Spec.atoms(
    select { ".cookieInfo" }  // [0]
            read "visibility"
            equalTo "visible",
    select { ".cookieInfo" }  // [1]
            read "visibility"
            equalTo "hidden",
    select { "button#close" } // [2]
            at "click"
)

Spec.record(
    after { "click" },
    after { "touch" }
)

// helper formulae
val screen = Spec.screen
val isVisible = Spec.atoms[0]
val isHidden = Spec.atoms[1]
val button = Spec.atoms[2]
val er1 = isVisible and screen
val innerEr2 = er1 and (button implies isHidden)
val er2 = eventually(innerEr2)
val er3 = everywhere(er2)

// Final formula
Spec.formula = er3
