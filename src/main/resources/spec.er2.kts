import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import eu.quanticol.moonlight.formula.spatial.SomewhereFormula
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
val somewhereAButton = SomewhereFormula(Spec.basicDistance, isCloseButton)
val er21 = impliesFormula(isVisible, somewhereAButton)
val er22 = impliesFormula(closeButtonIsActive, isHidden)

// Final formula
Spec.formula = AndFormula(er21, er22)
