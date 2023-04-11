import at.ac.tuwien.trustcps.dsl.*

Spec.atoms(
    select { ".tab__nav__item:first-child" },  // [0]
    //read "height"
    select { ".tab__nav__item:last-child" },  // [1]
    // read "height"
)

Spec.record(
    after { "click" },
    after { "touch" }
)

// helper formulae
val screen = Spec.screen
val firstTab = Spec.atoms[0]
val lastTab = Spec.atoms[1]
val requirement = firstTab or lastTab

// Final formula
Spec.formula = requirement
