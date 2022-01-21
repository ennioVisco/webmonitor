package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checker.Checker
import com.tylerthrailkill.helpers.prettyprint.pp
import org.openqa.selenium.Dimension
import at.ac.tuwien.trustcps.tracker.Browser
import at.ac.tuwien.trustcps.tracker.PageTracker
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
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

//    val output = Reporter()
//    output.report(result, "fromJava Quantitative")

    print("Ending...")
    println(Calendar.getInstance().time)
}

private fun spec(): Formula {
    return AtomicFormula("#cookieman-modal p")
}

private fun tracking(): Map<String, String> {
    val baseUrl = URL(URL)

    val tracker = PageTracker(baseUrl, Dimension(WIDTH, HEIGHT), Browser.CHROME)
    tracker.select("#cookieman-modal p")

    return tracker.track()
}

private fun checking(data: Map<String, String>, spec: Formula)
: SpatialTemporalSignal<Boolean>?
{
    val checker = Checker(WIDTH, HEIGHT, data)
    return checker.check(spec)
}

//{
//    "wnd_height" -> "1032",
//    "#cookieman-modal p::y" -> "122",
//    "#cookieman-modal p::x" -> "31",
//    "vp_height" -> "147",
//    "#cookieman-modal p::height" -> "161",
//    "vp_width" -> "500",
//    "wnd_width" -> "1920",
//    "#cookieman-modal p::width" -> "421"
//}
