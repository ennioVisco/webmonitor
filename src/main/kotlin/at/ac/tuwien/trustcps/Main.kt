package at.ac.tuwien.trustcps

import at.ac.tuwien.trustcps.checker.Checker
import at.ac.tuwien.trustcps.reporter.Reporter
import at.ac.tuwien.trustcps.tracker.Browser
import at.ac.tuwien.trustcps.tracker.PageTracker
import com.tylerthrailkill.helpers.prettyprint.pp
import eu.quanticol.moonlight.formula.*
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import org.openqa.selenium.Dimension
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
    val result: SpatialTemporalSignal<Boolean> = checking(data, spec())

    val output = Reporter(HEIGHT, WIDTH)
    output.plot(result, "fromJava Quantitative")

    print("Ending...")
    println(Calendar.getInstance().time)
}

private fun spec(): Formula {
    val screen = AtomicFormula("screen")
    val cookieInfo = AtomicFormula("#cookieman-modal p")
    val infoOnScreen = impliesFormula(cookieInfo, screen)
    //return EverywhereFormula("base", infoOnScreen)
    return GloballyFormula(infoOnScreen)
}

private fun impliesFormula(left: Formula, right: Formula): Formula {
    return OrFormula(NegationFormula(left), right)
}

private fun tracking(): Map<String, String> {
    val baseUrl = URL(URL)

    val tracker = PageTracker(baseUrl, Dimension(WIDTH, HEIGHT), Browser.CHROME)
    tracker.select("#cookieman-modal p")

    return tracker.track()
}

private fun checking(data: Map<String, String>, spec: Formula)
        : SpatialTemporalSignal<Boolean> {
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
