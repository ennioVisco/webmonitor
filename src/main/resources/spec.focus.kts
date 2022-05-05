import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.temporal.GloballyFormula
import org.researchr.conf.ase2022.NotFormula
import org.researchr.conf.ase2022.Spec
import org.researchr.conf.ase2022.impliesFormula

Spec.atoms = listOf(
    ".carousel.slide:focus",
    ".carousel.slide:slide",
)

// helper formulae
val screen = AtomicFormula("screen")
val sliderFocus = AtomicFormula(Spec.atoms[0])
val sliderSlided = AtomicFormula(Spec.atoms[1])

// Final formula
Spec.formula = impliesFormula(sliderFocus, GloballyFormula(NotFormula(sliderSlided)))
