import at.ac.tuwien.trustcps.dsl.*

Spec.atoms(
    select { ".btn-open-survey" },  // [0]
    //read "height"
//    select { ".left-bordered-title" },  // [1]
    // read "height"
    select { ".colored-block__title.clearfix" },
    // .quick-search--input
//    select { "a[data-target='global-menu']" }

    select { ".item-meta-row p" } read "height" greaterThan 320,
    select { ".item-meta-row p" } read "width" greaterThan 256,
    select { ":is(.item-meta-row p):nth-of-type(1)" },
)

//Spec.record(
//    after { "click" },
//    after { "touch" }
//)

// helper formulae
val screen = Spec.screen
val feedback = Spec.atoms[0]
val title = Spec.atoms[1]
val pHeight = Spec.atoms[2]
val pWidth = Spec.atoms[3]

val titleAndFeedbackOverlap = not(title and feedback and screen)
val reflow = not(pHeight and pWidth)

// Final formula
Spec.formula = titleAndFeedbackOverlap
