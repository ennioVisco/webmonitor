package at.ac.tuwien.trustcps

import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import eu.quanticol.moonlight.formula.classic.NegationFormula
import eu.quanticol.moonlight.formula.classic.OrFormula
import eu.quanticol.moonlight.formula.spatial.EverywhereFormula

object Spec {
    private const val distance = "base"

    // atomic propositions
    val atoms = listOf(
        ".dialog",
        "h1"
    )

    // helper formulae
    private val screen = AtomicFormula("screen")
    private val cookieInfo = AtomicFormula(atoms[0])
    private val h1 = AtomicFormula(atoms[1])
    private val cookieOnScreen = AndFormula(cookieInfo, screen)
    private val allCookieOnScreen = EverywhereFormula(distance, cookieOnScreen)

    // final spec to monitor
    val formula = OrFormula(h1, AndFormula(cookieInfo, NegationFormula(screen)))

//    private fun spec(): Formula {
//        val screen = AtomicFormula("screen")
//        val cookieInfo = AtomicFormula(elements[0])
//        return EverywhereFormula(distance, AndFormula(cookieInfo, screen))
//        //return AndFormula(cookieInfo, screen)
//    }

    //  const val ELEMENT = "#cookieman-modal p"
//const val ELEMENT = ".modal-content"
}