import eu.quanticol.moonlight.formula.AtomicFormula
import org.researchr.conf.ase2022.NotFormula
import org.researchr.conf.ase2022.Spec
import org.researchr.conf.ase2022.impliesFormula

Spec.atoms = listOf(
    ".dialog\$visibility = visible",
    "#close",
    "#close:active"
)

// helper formulae
val screen = AtomicFormula("screen")
val isVisible = AtomicFormula(Spec.atoms[0])
val isHidden = NotFormula(isVisible)
val closeButtonIsActive = AtomicFormula(Spec.atoms[2])
val er2 = impliesFormula(closeButtonIsActive, isHidden)

// Final formula
Spec.formula = er2
