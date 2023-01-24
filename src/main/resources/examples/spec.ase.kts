import at.ac.tuwien.trustcps.dsl.*

Spec.atoms = listOf(
    select { ".carousel" } read "visibility" equalTo "visible",
    select { ".col-sm-12 p" } read "width" greaterThan 256,
    select { ".col-sm-12 p" } read "height" greaterThan 320,
    select { ".item.active .carousel-caption p" } read "height" lessThan 150,
    select { ".item.active" }
)

// helper formulae
val screen = Spec.screen
val slider = Spec.atoms[0]
val pWidth = Spec.atoms[1]
val pHeight = Spec.atoms[2]
val cCaption = Spec.atoms[3]
val isActive = Spec.atoms[4]

// Final formula
//Spec.formula = NegationFormula(AndFormula(pWidth, pHeight))
Spec.formula = isActive implies cCaption
