package at.ac.tuwien.trustcps

import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.formula.classic.AndFormula
import eu.quanticol.moonlight.formula.spatial.EverywhereFormula

const val distance = "base"

object Spec {
    // atomic propositions
    val atoms = listOf(
        ".dialog"
    )

    // helper formulae
    private val screen = AtomicFormula("screen")
    private val cookieInfo = AtomicFormula(atoms[0])
    private val cookieOnScreen = AndFormula(cookieInfo, screen)
    private val allCookieOnScreen = EverywhereFormula(distance, cookieOnScreen)

    // final spec to monitor
    val formula = cookieOnScreen

//    private fun spec(): Formula {
//        val screen = AtomicFormula("screen")
//        val cookieInfo = AtomicFormula(elements[0])
//        return EverywhereFormula(distance, AndFormula(cookieInfo, screen))
//        //return AndFormula(cookieInfo, screen)
//    }

    //  const val ELEMENT = "#cookieman-modal p"
//const val ELEMENT = ".modal-content"
}