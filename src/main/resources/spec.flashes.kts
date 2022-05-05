import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.OrFormula
import eu.quanticol.moonlight.formula.temporal.GloballyFormula
import org.researchr.conf.ase2022.Spec

Spec.atoms = listOf(
    "body\$background-color = white",
    "body\$background-color = whitesmoke",
)

// helper formulae
val screen = AtomicFormula("screen")
val isWhite = AtomicFormula(Spec.atoms[0])
val isWhiteSmoke = AtomicFormula(Spec.atoms[1])

// Final formula
Spec.formula = GloballyFormula(OrFormula(isWhite, isWhiteSmoke))
