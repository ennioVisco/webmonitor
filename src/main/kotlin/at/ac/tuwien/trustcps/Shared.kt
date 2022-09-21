package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.dsl.*
import at.ac.tuwien.trustcps.tracking.*
import eu.quanticol.moonlight.core.formula.*
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.formula.classic.*
import eu.quanticol.moonlight.offline.signal.*

typealias GridSignal = SpatialTemporalSignal<Boolean>

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

    var record = emptyList<Event>()

    fun record(vararg events: Event) {
        record = events.toList()
    }

    fun atoms(vararg selectors: Selector) {
        atoms = selectors.toList()
    }

    fun atomsAsIds(): List<String> {
        return atoms.map { it.toString() }
    }

    val screen = AtomicFormula("screen")
}
