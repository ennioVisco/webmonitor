import com.enniovisco.dsl.*

Spec.atoms(
    select { "body" },  // [0]
//    select { ".btn-open-survey" },  // [0]
    //read "height"
//    select { ".left-bordered-title" },  // [1]
    // read "height"
    select { ".colored-block__title.clearfix" },
    // .quick-search--input
//    select { "a[data-target='global-menu']" }

    select { ".item-meta-row p" } read "height" greaterThan 320,
    select { ".item-meta-row p" } read "width" greaterThan 256,
    select { ":is(.item-meta-row p):nth-of-type(1)" },
    select { ".owl-item.active" } read "color" bind "bg",
    select { ".owl-item.active" },
)

Spec.record(
    after { "click" },
//    after { "touch" }
)

// helper formulae
val screen = Spec.screen
val feedback = Spec.atoms[0]
val title = Spec.atoms[1]
val pHeight = Spec.atoms[2]
val pWidth = Spec.atoms[3]
val owlItemBgColor = Spec.atoms[5]
val owlItem = Spec.atoms[6]

val titleAndFeedbackOverlap = not(title and feedback and screen)
val reflow = not(pHeight and pWidth)
val flashing = globally(owlItemBgColor)

// Final formula
Spec.formula = owlItem // or flashing
