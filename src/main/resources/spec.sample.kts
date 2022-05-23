import at.ac.tuwien.trustcps.*
import eu.quanticol.moonlight.formula.classic.*
import eu.quanticol.moonlight.formula.spatial.*

Spec.atoms = listOf(
    select { ".dialog" },
    select { "h1" }
)

// helper formulae
val screen = Spec.screen
val cookieInfo = Spec.atoms[0]
val h1 = Spec.atoms[1]
val cookieOnScreen = AndFormula(cookieInfo, screen)
val allCookieOnScreen = EverywhereFormula(Spec.basicDistance, cookieOnScreen)

// Final formula
Spec.formula = OrFormula(h1, AndFormula(cookieInfo, NegationFormula(screen)))
