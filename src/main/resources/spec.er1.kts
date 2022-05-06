import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import org.researchr.conf.ase2022.Spec

Spec.atoms = listOf(
    ".dialog\$visibility = visible",
)

// helper formulae
val screen = AtomicFormula("screen")
val isVisible = AtomicFormula(Spec.atoms[0])

// Final formula
Spec.formula = AndFormula(isVisible, screen)
