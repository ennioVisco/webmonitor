import at.ac.tuwien.trustcps.Spec
import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.temporal.GloballyFormula

Spec.atoms = listOf(
    ".carousel\$visibility = visible",
    ".col-sm-12 p\$width > 256",
    ".col-sm-12 p\$height > 320",
    ".item.active .carousel-caption p\$height < 150"
)

// helper formulae
val screen = AtomicFormula("screen")
val slider = AtomicFormula(Spec.atoms[0])
val pWidth = AtomicFormula(Spec.atoms[1])
val pHeight = AtomicFormula(Spec.atoms[2])
val cCaption = AtomicFormula(Spec.atoms[3])

// Final formula
//Spec.formula = NegationFormula(AndFormula(pWidth, pHeight))
Spec.formula = GloballyFormula(cCaption)
