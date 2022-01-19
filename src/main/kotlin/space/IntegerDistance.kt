package space

import eu.quanticol.moonlight.formula.DistanceDomain

class IntegerDistance : DistanceDomain<Int> {
    override fun zero(): Int {
        return 0
    }

    override fun infinity(): Int {
        return 1 / 0
    }

    override fun lessOrEqual(x: Int, y: Int): Boolean {
        return x < y || equalTo(x, y)
    }

    override fun sum(x: Int, y: Int): Int {
        return x + y
    }

    override fun equalTo(x: Int, y: Int): Boolean {
        return Math.abs(x - y) < 1.0E-12
    }

    override fun less(x: Int, y: Int): Boolean {
        return x < y
    }
}
