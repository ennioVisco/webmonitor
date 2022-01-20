package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checker.Checker
import at.ac.tuwien.trustcps.reporter.Reporter
import com.tylerthrailkill.helpers.prettyprint.pp
import eu.quanticol.moonlight.formula.BooleanDomain
import eu.quanticol.moonlight.formula.DoubleDomain
import eu.quanticol.moonlight.formula.Interval
import eu.quanticol.moonlight.monitoring.temporal.TemporalMonitor
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.util.TestUtils
import org.openqa.selenium.Dimension
import at.ac.tuwien.trustcps.tracker.Browser
import at.ac.tuwien.trustcps.tracker.PageTracker
import java.net.URL


private const val WIDTH = 320
private const val HEIGHT = 280
private const val URL = "https://tuwien.ac.at/"

fun main() {
    logger.info{"Starting process..."}
    val data = tracking()
    data.pp()
    monitoring(data)
}

private fun tracking(): Map<String, String> {
    val baseUrl = URL(URL)

    val tracker = PageTracker(baseUrl,
                              Dimension(WIDTH, HEIGHT),
                              Browser.CHROME)
    tracker.select("#cookieman-modal p")

    return tracker.track()
}

//{
//    "wnd_height" -> "147",
//    "#cookieman-modal p::y" -> "122",
//    "#cookieman-modal p::x" -> "31",
//    "vp_height" -> "147",
//    "#cookieman-modal p::height" -> "161",
//    "vp_width" -> "500",
//    "wnd_width" -> "500",
//    "#cookieman-modal p::width" -> "421"
//}

private fun monitoring(data: Map<String, String>) {
    val checker = Checker(WIDTH, HEIGHT, data)
    val s = checker.signal
    println("Finished executing")
    // if Grid.toArray($query::x, $query::y) then #cookieman-modal p = true
}



//private fun createSignal(): Signal<Int> {
//    val s = SpatialTemporalSignal<Int>(WIDTH * HEIGHT)
//
//    {
//        var time: Double = start
//        while (time < end) {
//            s.add(time, Function<Int, T> { i: Int? -> f.apply(time, i) })
//            time += dt
//        }
//    }
//
//    s.add(end, Function<Int, T> { i: Int? -> f.apply(end, i) })
//    return s
//}

private fun fromJava() {
    val output = Reporter()

    // Get signal
    val signal: Signal<Pair<Double, Double>> = TestUtils.createSignal(0.0, 50.0, 1.0) { x -> Pair(x, 3 * x) }


    // Build the property (Boolean Semantics)
    val mB: TemporalMonitor<Pair<Double, Double>, Boolean> = TemporalMonitor.globallyMonitor(
        TemporalMonitor.atomicMonitor { x -> x.first > x.second }, BooleanDomain(), Interval(0.0, 0.2)
    )

    // Monitoring
    val soutB: Signal<Boolean> = mB.monitor(signal)
    output.report(soutB, "fromJava Boolean")

    // Build the property (Quantitative Semantics)
    val mQ: TemporalMonitor<Pair<Double, Double>, Double> = TemporalMonitor.globallyMonitor(
        TemporalMonitor.atomicMonitor { x -> x.first - x.second }, DoubleDomain(), Interval(0.0, 0.2)
    )

    // Monitoring
    val soutQ: Signal<Double> = mQ.monitor(signal)
    output.report(soutQ, "fromJava Quantitative")
}




