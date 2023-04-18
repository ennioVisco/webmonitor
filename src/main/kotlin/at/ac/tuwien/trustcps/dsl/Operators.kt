package at.ac.tuwien.trustcps.dsl

import eu.quanticol.moonlight.core.base.*
import eu.quanticol.moonlight.core.formula.*
import eu.quanticol.moonlight.formula.classic.*
import eu.quanticol.moonlight.formula.spatial.*
import eu.quanticol.moonlight.formula.temporal.*

// Interval shorthand
typealias interval = Box<Int>

val Spec = at.ac.tuwien.trustcps.Spec

// Classical logic operators
infix fun Formula.implies(right: Formula) =
    OrFormula(NegationFormula(this), right)

infix fun Formula.or(right: Formula): Formula = OrFormula(this, right)
infix fun Formula.and(right: Formula): Formula = AndFormula(this, right)
fun not(argument: Formula): Formula = NegationFormula(argument)

// Temporal operators
fun eventually(argument: Formula): EventuallyFormula =
    EventuallyFormula(argument)

fun globally(argument: Formula): GloballyFormula = GloballyFormula(argument)
infix fun Formula.until(right: Formula): UntilFormula =
    UntilFormula(this, right)

// Temporal operators intervals
infix fun EventuallyFormula.within(interval: Interval) =
    EventuallyFormula(this.argument, interval)

infix fun GloballyFormula.within(interval: Interval) =
    GloballyFormula(this.argument, interval)

infix fun UntilFormula.within(interval: Interval) =
    UntilFormula(this.firstArgument, this.secondArgument, interval)

// Spatial operators
fun everywhere(argument: Formula): EverywhereFormula =
    EverywhereFormula(Spec.basicDistance, argument)

fun somewhere(argument: Formula): SomewhereFormula =
    SomewhereFormula(Spec.basicDistance, argument)

infix fun Formula.reach(right: Formula): ReachFormula =
    ReachFormula(this, Spec.basicDistance, right)

/*
// Spatial operators distances
infix fun SomewhereFormula.within(distanceInterval: interval) =
    SomewhereFormula(addDistanceFunction(distanceInterval), this.argument)

infix fun EverywhereFormula.within(distanceInterval: interval) =
    EverywhereFormula(addDistanceFunction(distanceInterval), this.argument)

infix fun ReachFormula.within(distanceInterval: interval) =
    ReachFormula(
        this.firstArgument,
        addDistanceFunction(distanceInterval),
        this.secondArgument
    )

private fun addDistanceFunction(interval: interval): String {
    TODO("Add distance function to the model)
    val id = interval.toString()
    //distanceFunctions.put(id) { intervalToDistance(interval, it) }
    return id
}

private fun intervalToDistance(
    interval: Box<Int>,
    model: RegularGridModel<Int>
):
        DistanceStructure<Int, Int> {
    return ManhattanDistanceStructure(
        { x: Int -> x },
        IntegerDomain(), interval.start, interval.end, model
    )
}
*/

fun select(@Language("css") cssQuery: String): Selector {
    return Selector(cssQuery)
}

fun select(init: () -> String): Selector {
    return Selector(init())
}

fun after(init: () -> String): Event {
    return Event(init())
}
