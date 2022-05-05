package org.researchr.conf.ase2022

import eu.quanticol.moonlight.core.formula.Formula
import eu.quanticol.moonlight.formula.classic.NegationFormula
import eu.quanticol.moonlight.formula.classic.OrFormula
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import org.researchr.conf.ase2022.tracking.Browser

typealias GridSignal = SpatialTemporalSignal<Boolean>
typealias NotFormula = NegationFormula

fun impliesFormula(left: Formula, right: Formula): Formula =
    OrFormula(NotFormula(left), right)

/**
 * Singleton object used to define the general settings of the browser session.
 */
object WebSource {
    /**
     * Width in pixels of the browser window the session should use.
     */
    var screenWidth = 1

    /**
     * Height in pixels of the browser window the session should use.
     */
    var screenHeight = 1

    /**
     * Time (in milliseconds) after which the recording is stopped.
     */
    var maxSessionDuration = 0L

    /**
     * URL that will be used to record the web page to analyze.
     */
    var targetUrl = ""

    /**
     * Browser engine to use for the session.
     */
    var browser = Browser.CHROME
}

/**
 * Singleton object used to define the specification to analyze.
 */
object Spec {
    /**
     * Distance metric to consider. Currently, only the default one is supported,
     * which consider distances in pixel terms.
     */
    const val basicDistance = "base"

    /**
     * List of atom labels to consider. These must be valid CSS selectors.
     */
    var atoms = emptyList<String>()

    /**
     * Formula that determines the specification to analyse.
     */
    var formula: Formula = NegationFormula(null)
}

fun parseSelector(queryString: String): List<String> {
    val sanitized = queryString
        .replace("\\s+".toRegex(), " ")
        .split('$', '<', '>', '=', limit = 3)
        .map { it.trim() }.toMutableList()
    if (sanitized[0].contains(":")) {
        sanitized[0] = sanitized[0].split(":")[0]
    }
    return if (sanitized.size < 2) {
        listOf(sanitized[0], "", "")
    } else {
        sanitized
    }
}