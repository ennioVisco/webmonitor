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

fun main() {
    val report = Reporter(toFile = true)

    report.mark("Tracking")
    val snapshots = tracking()



    report.mark("Checking")
    for ((pos, data) in snapshots.withIndex()) {
        val grid = generateSpatialModel(data)
        val result = checking(grid, data, Spec.formula)
        report.report(result, "output dump")

        report.mark("Plotting results")
        report.plot(pos, result, grid, "Grid plot")
    }

    report.mark("Ending")
}

private fun tracking(): List<Map<String, String>> {
    val baseUrl = URL(Target.targetUrl)
    val dimensions = Dimension(Target.screenWidth, Target.screenHeight)
    val tracker = PageTracker(
        baseUrl, dimensions, Browser.CHROME,
        maxSessionDuration = Target.MAX_SESSION_DURATION, toFile = true
    )

    Spec.atoms.forEach { tracker.select(it) }

    return tracker.track()
}

private fun checking(grid: Grid, data: Map<String, String>, spec: Formula)
        : GridSignal {
    val checker = Checker(grid, listOf(data), Spec.atoms)
    return checker.check(spec)
}


private fun generateSpatialModel(data: Map<String, String>): Grid {
    return if (data.containsKey("lvp_width")
        && data.containsKey("lvp_height")
    ) {
        Grid(
            rows = data["lvp_height"]!!.toInt(),
            columns = data["lvp_width"]!!.toInt()
        )
    } else {
        Grid(rows = Target.screenHeight, columns = Target.screenWidth)
    }
}
