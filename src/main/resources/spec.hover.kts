import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import eu.quanticol.moonlight.formula.temporal.EventuallyFormula
import eu.quanticol.moonlight.formula.temporal.GloballyFormula
import org.researchr.conf.ase2022.NotFormula
import org.researchr.conf.ase2022.Spec
import org.researchr.conf.ase2022.impliesFormula

Spec.atoms = listOf(
    ".dropdown:hover",
    ".dropdown-menu\$visibility = visible",
    ".dropdown-menu:hover"
)

// helper formulae
val screen = AtomicFormula("screen")
val divIsHover = AtomicFormula(Spec.atoms[0])
val tooltipVisible = AtomicFormula(Spec.atoms[1])
val tooltipIsHover = AtomicFormula(Spec.atoms[2])

// Final formula
Spec.formula = impliesFormula(
    AndFormula(divIsHover, NotFormula(tooltipVisible)),
    AndFormula(
        GloballyFormula(tooltipVisible),
        impliesFormula(EventuallyFormula(tooltipIsHover), divIsHover)
    )
)
