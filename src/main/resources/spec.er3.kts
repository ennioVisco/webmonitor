import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import eu.quanticol.moonlight.formula.temporal.EventuallyFormula
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
val isCloseButton = AtomicFormula(Spec.atoms[1])
val closeButtonIsActive = AtomicFormula(Spec.atoms[2])
val er1 = AndFormula(isVisible, screen)
val er2 = impliesFormula(closeButtonIsActive, isHidden)

// Final formula
Spec.formula = EventuallyFormula(AndFormula(er1, er2))
