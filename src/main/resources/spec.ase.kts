import at.ac.tuwien.trustcps.Spec

import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import eu.quanticol.moonlight.formula.spatial.EverywhereFormula

Spec.atoms = listOf(
    ".carousel",
    "h1"
)

// helper formulae
val screen = AtomicFormula("screen")
val slider = AtomicFormula(Spec.atoms[0])
val h1 = AtomicFormula(Spec.atoms[1])
val cookieOnScreen = AndFormula(slider, screen)
val allCookieOnScreen = EverywhereFormula(Spec.basicDistance, cookieOnScreen)

// Final formula
Spec.formula = slider
