import at.ac.tuwien.trustcps.grid.Grid
import com.tylerthrailkill.helpers.prettyprint.pp
import eu.quanticol.moonlight.formula.BooleanDomain
import eu.quanticol.moonlight.formula.DoubleDomain
import eu.quanticol.moonlight.formula.Interval
import eu.quanticol.moonlight.monitoring.temporal.TemporalMonitor
import eu.quanticol.moonlight.signal.DataHandler.*
import eu.quanticol.moonlight.signal.DataHandler.REAL
import eu.quanticol.moonlight.signal.Signal
//import eu.quanticol.moonlight.util.Pair
import eu.quanticol.moonlight.util.TestUtils
import mu.KotlinLogging
import java.time.LocalDateTime
import javax.activation.DataHandler


private val logger = KotlinLogging.logger {}
fun main() {
    println(LocalDateTime.now().toString())
    //logger.warn("Starting")
    //val model = Grid.getModel(192, 108);
    val model = Grid.getModel(1920, 1080)
    //model.pp()
    println("Model: " +  model.size())
    println(LocalDateTime.now().toString())
    //logger.warn("Finished!")
}


//fun main() {
//    fromJava()
//}

private fun fromJava() {
    // Get signal
    val signal: Signal<Pair<Double, Double>> = TestUtils.createSignal(0.0, 50.0, 1.0) { x -> Pair(x, 3 * x) }


    // Build the property (Boolean Semantics)
    val mB: TemporalMonitor<Pair<Double, Double>, Boolean> = TemporalMonitor.globallyMonitor(
        TemporalMonitor.atomicMonitor { x -> x.first > x.second }, BooleanDomain(), Interval(0.0, 0.2)
    )

    // Monitoring
    val soutB: Signal<Boolean> = mB.monitor(signal)
    val monitorValuesB: Array<DoubleArray> = soutB.arrayOf(::bDoubleOf)
    // Print results
    print("fromJava Boolean\n")
    printResults(monitorValuesB)

    // Build the property (Quantitative Semantics)
    val mQ: TemporalMonitor<Pair<Double, Double>, Double> = TemporalMonitor.globallyMonitor(
        TemporalMonitor.atomicMonitor { x -> x.first - x.second }, DoubleDomain(), Interval(0.0, 0.2)
    )
    val soutQ: Signal<Double> = mQ.monitor(signal)
    val monitorValuesQ: Array<DoubleArray> = soutQ.arrayOf(::rDoubleOf)
    // Print results
    print("fromJava Quantitative \n")
    printResults(monitorValuesQ)
}

private fun printResults(monitorValues: Array<DoubleArray>) {
    for (i in monitorValues.indices) {
        for (j in monitorValues[i].indices) {
            print(monitorValues[i][j])
            print(" ")
        }
        println("")
    }
}

private fun bDoubleOf(aBoolean: Any?): Double {
    return if (aBoolean is Boolean) {
        if (aBoolean) 1.0 else -1.0
    } else Double.NaN
}

private fun rDoubleOf(aDouble: Any?): Double {
    return if (aDouble is Double) {
        aDouble
    } else Double.NaN
}