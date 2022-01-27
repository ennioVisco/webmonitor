package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checker.Checker
import at.ac.tuwien.trustcps.checker.impliesFormula
import at.ac.tuwien.trustcps.reporter.Reporter
import at.ac.tuwien.trustcps.space.Grid
import at.ac.tuwien.trustcps.tracker.Browser
import at.ac.tuwien.trustcps.tracker.PageTracker
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import org.openqa.selenium.Dimension
import java.net.URL

typealias GridSignal = SpatialTemporalSignal<Boolean>

private const val WIDTH = 320
private const val HEIGHT = 280
private const val URL = "https://tuwien.ac.at/"





/**
 * ## Example of output
 * ```
 * {
 *   "wnd_height" -> "1032",
 *   "#cookieman-modal p::y" -> "122",
 *   "#cookieman-modal p::x" -> "31",
 *   "vp_height" -> "147",
 *   "#cookieman-modal p::height" -> "161",
 *   "vp_width" -> "500",
 *   "wnd_width" -> "1920",
 *   "#cookieman-modal p::width" -> "421"
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

    report.mark("Plotting results")
    report.plot(result, "Grid plot")

    report.mark("Ending")
}

private fun spec(): Formula {
    val screen = AtomicFormula("screen")
    val cookieInfo = AtomicFormula("#cookieman-modal p")
    val infoOnScreen = impliesFormula(cookieInfo, screen)
    //return EverywhereFormula("base", infoOnScreen)
    return GloballyFormula(infoOnScreen)
}

private fun tracking(): Map<String, String> {
    val baseUrl = URL(URL)

    val tracker = PageTracker(baseUrl, Dimension(WIDTH, HEIGHT), Browser.CHROME)
    tracker.select("#cookieman-modal p")

    return tracker.track()
}

private fun checking(grid: Grid, data: Map<String, String>, spec: Formula)
: GridSignal
{
    val checker = Checker(grid, listOf(data))
    return checker.check(spec)
}
