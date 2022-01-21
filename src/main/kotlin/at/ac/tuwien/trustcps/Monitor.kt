package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checker.Checker
import at.ac.tuwien.trustcps.reporter.Reporter
import com.tylerthrailkill.helpers.prettyprint.pp
import eu.quanticol.moonlight.monitoring.temporal.TemporalMonitor
import eu.quanticol.moonlight.signal.Signal
import eu.quanticol.moonlight.util.TestUtils
import org.openqa.selenium.Dimension
import at.ac.tuwien.trustcps.tracker.Browser
import at.ac.tuwien.trustcps.tracker.PageTracker
import eu.quanticol.moonlight.formula.*
import java.net.URL
import java.util.*


private const val WIDTH = 320
private const val HEIGHT = 280
private const val URL = "https://tuwien.ac.at/"

fun main() {
    print("Starting tracking...")
    println(Calendar.getInstance().time)
    val data = tracking()
    data.pp()

    print("Starting checking...")
    println(Calendar.getInstance().time)
    checking(data, spec())

    print("Ending...")
    println(Calendar.getInstance().time)
}

private fun spec(): Formula {
    return AtomicFormula("#cookieman-modal p")
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

private fun checking(data: Map<String, String>, spec: Formula) {
    val checker = Checker(WIDTH, HEIGHT, data)
    checker.check(spec).pp()
    println("Finished executing")
    // if Grid.toArray($query::x, $query::y) then #cookieman-modal p = true
}

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




