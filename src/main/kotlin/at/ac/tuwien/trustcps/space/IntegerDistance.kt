package at.ac.tuwien.trustcps.space

import eu.quanticol.moonlight.core.space.DistanceDomain
import kotlin.math.abs

class IntegerDistance : DistanceDomain<Int> {
    override fun zero() = 0

    override fun infinity() = Int.MAX_VALUE

    override fun sum(x: Int, y: Int) = x + y

    override fun less(x: Int, y: Int) = x < y

    override fun lessOrEqual(x: Int, y: Int) =  less( x, y) || equalTo(x, y)

    override fun equalTo(x: Int, y: Int) = abs(x - y) < 1.0E-12
}
