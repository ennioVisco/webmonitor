package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checking.Checker
import at.ac.tuwien.trustcps.reporting.Reporter
import at.ac.tuwien.trustcps.space.Grid
import at.ac.tuwien.trustcps.tracking.Browser
import at.ac.tuwien.trustcps.tracking.PageTracker
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import org.openqa.selenium.Dimension
import java.net.URL

typealias GridSignal = SpatialTemporalSignal<Boolean>

private const val WIDTH = 320
private const val HEIGHT = 280
private const val URL = "https://tuwien.ac.at/"
const val ELEMENT = "#cookieman-modal p"

/**
 * ### Example of output
 * ```
 * {
 *   "wnd_height" -> "1032",
 *   "vp_height" -> "147",
 *   "vp_width" -> "500",
 *   "wnd_width" -> "1920",
 *   "#cookieman-modal p::x" -> "31",
 *   "#cookieman-modal p::y" -> "122",
 *   "#cookieman-modal p::width" -> "421"
 *   "#cookieman-modal p::height" -> "161",
 * }
 * ```
 */
fun main() {
    val grid = Grid(HEIGHT, WIDTH)
    val report = Reporter(grid)

    report.mark("Tracking")
    val data = tracking()

    report.mark("Checking")
    val result = checking(grid, data, spec())
    //report.report(result, "output dump")

    report.mark("Plotting results")
    report.plot(result, "Grid plot")

    report.mark("Ending")
}

private fun tracking(): Map<String, String> {
    val baseUrl = URL(URL)

    val tracker = PageTracker(baseUrl, Dimension(WIDTH, HEIGHT), Browser.CHROME)
    tracker.select(ELEMENT)

    return tracker.track()
}

private fun spec(): Formula {
    val screen = AtomicFormula("screen")
    val cookieInfo = AtomicFormula(ELEMENT)
    return AndFormula(cookieInfo, screen)
}

private fun checking(grid: Grid, data: Map<String, String>, spec: Formula)
: GridSignal
{
    val checker = Checker(grid, listOf(data), listOf(ELEMENT))
    return checker.check(spec)
}

