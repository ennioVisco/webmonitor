package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checking.*
import at.ac.tuwien.trustcps.reporting.*
import at.ac.tuwien.trustcps.space.*
import at.ac.tuwien.trustcps.tracking.*
import org.openqa.selenium.*
import java.net.*
import javax.script.*

fun main(args: Array<String>) {
    validateArgs(args)

    val report = Reporter(toFile = true)

    report.mark("Tracking")
    val snapshots = tracking()

    report.mark("Checking")
    val grid = generateSpatialModel(snapshots[0])
    val result = checking(grid, snapshots)
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
            loadScripts(source, spec = source)
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

    Spec.atomsAsIds().forEach { tracker.select(it) }
    Spec.record.forEach { tracker.record(it.asPair()) }

    return tracker.run()
}

private fun checking(grid: Grid, data: List<Map<String, String>>): GridSignal {
    val checker = Checker(grid, data, Spec.atomsAsIds())
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
