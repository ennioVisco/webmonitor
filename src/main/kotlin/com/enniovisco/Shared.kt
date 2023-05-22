package com.enniovisco

import com.enniovisco.dsl.*
import eu.quanticol.moonlight.core.formula.*
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.formula.classic.*
import eu.quanticol.moonlight.offline.signal.*

internal typealias GridSignal = SpatialTemporalSignal<Boolean>

/**
 * Enum of the supported browsers.
 */
typealias Browser = com.enniovisco.tracking.Browser

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
     * Waiting time before starting to record. Use this to give time to the
     * page to load all the initial content, required by heavy pages and slow
     * connections
     */
    var wait = 1_000L

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
    var atoms = emptyList<Selector>()

    /**
     * Formula that determines the specification to analyse.
     */
    var formula: Formula = NegationFormula(null)

    /**
     * List of events that trigger the recording.
     */
    internal var record = emptyList<Event>()


    /**
     * Sets the list of events triggering the recording.
     */
    fun record(vararg events: Event) {
        record = events.toList()
    }

    /**
     * Sets the list of atoms to consider.
     */
    fun atoms(vararg selectors: Selector) {
        atoms = selectors.toList()
    }

    internal fun atomsAsIds(): List<String> {
        return atoms.map { it.toString() }
    }

    /**
     * Helper atom to represent the screen.
     */
    val screen = AtomicFormula("screen")
}
