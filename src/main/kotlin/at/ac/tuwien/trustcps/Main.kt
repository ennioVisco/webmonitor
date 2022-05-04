package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checking.Checker
import at.ac.tuwien.trustcps.reporting.Reporter
import at.ac.tuwien.trustcps.space.Grid
import at.ac.tuwien.trustcps.tracking.PageTracker
import eu.quanticol.moonlight.core.formula.Formula
import org.openqa.selenium.Dimension
import java.net.URL
import javax.script.ScriptEngineManager


fun main(args: Array<String>) {
    validateArgs(args)

    val report = Reporter(toFile = true)

    report.mark("Tracking")
    val snapshots = tracking()

    report.mark("Checking")
    val grid = generateSpatialModel(snapshots[0])
    val result = checking(grid, snapshots, Spec.formula)
    report.report(result, "output dump")

    for ((pos, _) in snapshots.withIndex()) {
        report.mark("Plotting results")
        report.plot(pos, result, grid, "Grid plot")
    }

    report.mark("Ending")
}

private fun validateArgs(args: Array<String>) {
    try {
        val (source, spec) = args
        loadScripts(source, spec)
    } catch (e: ArrayIndexOutOfBoundsException) {
        try {
            val (source) = args
            loadScripts(source, source)
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw IllegalArgumentException("No source or spec provided.")
        }
    }
}

private fun loadScripts(source: String, spec: String) {
    with(ScriptEngineManager().getEngineByExtension("kts")) {
        eval(loadResource("source.$source.kts"))
        eval(loadResource("spec.$spec.kts"))
    }
}

private fun loadResource(name: String) =
    object {}.javaClass.classLoader.getResourceAsStream(name)?.bufferedReader()

private fun tracking(): List<Map<String, String>> {
    val baseUrl = URL(WebSource.targetUrl)
    val dimensions = Dimension(WebSource.screenWidth, WebSource.screenHeight)
    val tracker = PageTracker(
        baseUrl, dimensions, WebSource.browser,
        maxSessionDuration = WebSource.maxSessionDuration, toFile = true
    )

    Spec.atoms.forEach { tracker.select(it) }

    return tracker.track()
}

private fun checking(grid: Grid, data: List<Map<String, String>>, spec: Formula)
        : GridSignal {
    val checker = Checker(grid, data, Spec.atoms)
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
        Grid(rows = WebSource.screenHeight, columns = WebSource.screenWidth)
    }
}
