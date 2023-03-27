import at.ac.tuwien.trustcps.dsl.*

Spec.atoms(
    select { ".btn-open-survey" },  // [0]
    //read "height"
//    select { ".left-bordered-title" },  // [1]
    // read "height"
    select { ".colored-block__title.clearfix" }
    // .quick-search--input
//    select { "a[data-target='global-menu']" }
)

//Spec.record(
//    after { "click" },
//    after { "touch" }
//)

// helper formulae
val screen = Spec.screen
val feedback = Spec.atoms[0]
val title = Spec.atoms[1]

// Final formula
Spec.formula = title and feedback and screen
