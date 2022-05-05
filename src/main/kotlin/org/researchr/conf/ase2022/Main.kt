package org.researchr.conf.ase2022

import org.researchr.conf.ase2022.checking.Checker
import org.researchr.conf.ase2022.reporting.Reporter
import org.researchr.conf.ase2022.space.Grid
import org.researchr.conf.ase2022.tracking.PageTracker
import org.openqa.selenium.Dimension
import java.net.URL
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {
    validateArgs(args)

    val report = Reporter(toFile = true)

    report.mark2("Tracking")
    val snapshots = tracking()

    report.mark2("Checking")
    val grid = generateSpatialModel(snapshots[0])
    val result = checking(grid, snapshots)
    report.report(result, "output dump")

    for ((pos, _) in snapshots.withIndex()) {
        report.mark("Plotting results")
        report.plot(pos, result, grid, "Grid plot")
    }

    report.mark2("Ending")
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

private fun checking(grid: Grid, data: List<Map<String, String>>): GridSignal {
    val checker = Checker(grid, data, Spec.atoms)
    return checker.check(Spec.formula)
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
