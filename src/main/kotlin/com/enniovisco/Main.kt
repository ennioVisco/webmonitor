package com.enniovisco

import com.enniovisco.checking.*
import com.enniovisco.cli.*
import com.enniovisco.reporting.*
import com.enniovisco.space.*
import com.enniovisco.tracking.*
import org.openqa.selenium.*
import java.net.*

private typealias ResultData = List<Map<String, String>>
private typealias Metadata = Map<String, String>

fun main(args: Array<String>) {
    main(args, preloaded = false, toFile = true, toConsole = false)
}

fun main(args: Array<String>, preloaded: Boolean = false, toFile : Boolean = true, toConsole: Boolean = false) {
    println("Running....")
    Cli(args, toFile = toFile, toConsole = toConsole, preloaded = preloaded) {
        it.title("Tracking")
        val snapshots = tracking(it)

        val grid = generateSpatialModel(snapshots[0])

        it.title("Checking")
        val result = checking(grid, snapshots)
        it.report(result, "output dump")

        it.title("Plotting results")
        for ((pos, _) in snapshots.withIndex()) {
            it.plot(pos, result, grid, "Grid plot $pos")
        }

        it.title("Ending")
    }
}

private fun tracking(report: Reporter): ResultData {
    val baseUrl = URI.create(WebSource.targetUrl).toURL()
    val dimensions = Dimension(
        WebSource.screenWidth,
        WebSource.screenHeight
    )
    val tracker = PageTracker(
        baseUrl,
        dimensions,
        WebSource.browser,
        wait = WebSource.wait,
        maxSessionDuration = WebSource.maxSessionDuration,
        toFile = true
    )

    Spec.atomsAsIds().forEach { tracker.select(it) }
    Spec.record.forEach { tracker.record(it.asPair()) }

    val results = tracker.run()

    processMetadata(tracker.metadata, report)

    return results
}

private fun processMetadata(
    metadata: Metadata,
    report: Reporter
) {
    report.devicePixelRatio = metadata["devicePixelRatio"]?.toDouble()
        ?: throw Error("No device pixel ratio found")
}

private fun checking(
    grid: Grid,
    data: List<Map<String, String>>
): GridSignal {
    val selectors =
        Spec.atoms.associate { Pair(it.toString(), it.modifier) }
    val checker = Checker(grid, data, selectors)
    return checker.check(Spec.formula)
}

private fun generateSpatialModel(data: Map<String, String>): Grid {
    // TODO: could switch between lvp and vvp
    val widthKey = "vvp_width"
    val heightKey = "vvp_height"
    return if (data.containsKey(widthKey) && data.containsKey(heightKey)) {
        Grid(
            rows = data[heightKey]!!.toInt(),
            columns = data[widthKey]!!.toInt()
        )
    } else {
        Grid(
            rows = WebSource.screenHeight,
            columns = WebSource.screenWidth
        )
    }
}
