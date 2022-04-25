package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checking.Checker
import at.ac.tuwien.trustcps.reporting.Reporter
import at.ac.tuwien.trustcps.space.Grid
import at.ac.tuwien.trustcps.tracking.Browser
import at.ac.tuwien.trustcps.tracking.PageTracker
import eu.quanticol.moonlight.core.formula.Formula
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import org.openqa.selenium.Dimension
import java.net.URL

typealias GridSignal = SpatialTemporalSignal<Boolean>

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
    val grid = Grid(Target.screenHeight, Target.screenWidth)
    val report = Reporter(grid, toFile = true)

    report.mark("Tracking")
    val data = tracking()

    report.mark("Checking")
    val result = checking(grid, data, Spec.formula)
    report.report(result, "output dump")

    report.mark("Plotting results")
    report.plot(result, "Grid plot")

    report.mark("Ending")
}

private fun tracking(): Map<String, String> {
    val baseUrl = URL(Target.targetUrl)
    val dimensions = Dimension(Target.screenWidth, Target.screenHeight)
    val tracker = PageTracker(baseUrl, dimensions, Browser.CHROME)

    Spec.atoms.forEach { tracker.select(it) }

    return tracker.track()
}

private fun checking(grid: Grid, data: Map<String, String>, spec: Formula)
        : GridSignal {
    val checker = Checker(grid, listOf(data), Spec.atoms)
    return checker.check(spec)
}

