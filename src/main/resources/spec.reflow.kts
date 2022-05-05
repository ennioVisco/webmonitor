import org.researchr.conf.ase2022.Spec
import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import org.researchr.conf.ase2022.NotFormula

Spec.atoms = listOf(
    ".carousel\$visibility = visible",
    ".col-sm-12 p\$width > 256",
    ".col-sm-12 p\$height > 320",
    ".item.active .carousel-caption p\$height < 150",
    ".item.active"
)

// helper formulae
val screen = AtomicFormula("screen")
val slider = AtomicFormula(Spec.atoms[0])
val pWidth = AtomicFormula(Spec.atoms[1])
val pHeight = AtomicFormula(Spec.atoms[2])
val cCaption = AtomicFormula(Spec.atoms[3])
val isActive = AtomicFormula(Spec.atoms[4])

// Final formula
Spec.formula = NotFormula(AndFormula(pWidth, pHeight))
