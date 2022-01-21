package at.ac.tuwien.trustcps

import com.tylerthrailkill.helpers.prettyprint.pp
import at.ac.tuwien.trustcps.tracker.PageTracker
import java.net.URL

fun main() {
    val baseUrl = URL("https://www.tuwien.ac.at/")

   val tracker = PageTracker(baseUrl)
    tracker.select("h1")
    tracker.select("h2")

    val data = tracker.track()
    data.pp()
}
