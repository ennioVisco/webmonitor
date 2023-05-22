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
    Cli(args, toFile = true) {
        it.title("Tracking")
        val snapshots = com.enniovisco.tracking(it)

        val grid = com.enniovisco.generateSpatialModel(snapshots[0])

        it.title("Checking")
        val result = com.enniovisco.checking(grid, snapshots)
        it.report(result, "output dump")

        it.title("Plotting results")
        for ((pos, _) in snapshots.withIndex()) {
            it.plot(pos, result, grid, "Grid plot $pos")
        }

        it.title("Ending")
    }
}

private fun tracking(report: Reporter): com.enniovisco.ResultData {
    val baseUrl = URL(com.enniovisco.WebSource.targetUrl)
    val dimensions = Dimension(
        com.enniovisco.WebSource.screenWidth,
        com.enniovisco.WebSource.screenHeight
    )
    val tracker = PageTracker(
        baseUrl,
        dimensions,
        com.enniovisco.WebSource.browser,
        wait = com.enniovisco.WebSource.wait,
        maxSessionDuration = com.enniovisco.WebSource.maxSessionDuration,
        toFile = true
    )

    com.enniovisco.Spec.atomsAsIds().forEach { tracker.select(it) }
    com.enniovisco.Spec.record.forEach { tracker.record(it.asPair()) }

    val results = tracker.run()

    com.enniovisco.processMetadata(tracker.metadata, report)

    return results
}

private fun processMetadata(
    metadata: com.enniovisco.Metadata,
    report: Reporter
) {
    report.devicePixelRatio = metadata["devicePixelRatio"]?.toDouble()
        ?: throw Error("No device pixel ratio found")
}

private fun checking(
    grid: Grid,
    data: List<Map<String, String>>
): com.enniovisco.GridSignal {
    val selectors =
        com.enniovisco.Spec.atoms.associate { Pair(it.toString(), it.modifier) }
    val checker = Checker(grid, data, selectors)
    return checker.check(com.enniovisco.Spec.formula)
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
            rows = com.enniovisco.WebSource.screenHeight,
            columns = com.enniovisco.WebSource.screenWidth
        )
    }
}
