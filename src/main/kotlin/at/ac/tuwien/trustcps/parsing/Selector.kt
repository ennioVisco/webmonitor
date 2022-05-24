package at.ac.tuwien.trustcps.parsing;

import eu.quanticol.moonlight.core.formula.Formula
import eu.quanticol.moonlight.core.formula.FormulaVisitor
import eu.quanticol.moonlight.formula.AtomicFormula

data class Selector(
    val queryString: String,
    var attribute: String = "",
    var comparison: Any? = null,
    var state: String = ""
) : Formula {
    private var op = ""

    operator fun compareTo(value: Any): Int {
        comparison = value
        return 0
    }

    infix fun read(attribute: String): Selector {
        this.attribute = attribute
        return this
    }
    
    infix fun at(state: String): Selector {
        this.state = state
        return this
    }

    infix fun equals(comparison: Any): Selector {
        this.comparison = comparison
        this.op = "="
        return this
    }

    infix fun greaterThan(comparison: Any): Selector {
        this.comparison = comparison
        this.op = ">"
        return this
    }

    infix fun greaterEqualsThan(comparison: Any): Selector {
        this.comparison = comparison
        this.op = ">="
        return this
    }

    infix fun lessThan(comparison: Any): Selector {
        this.comparison = comparison
        this.op = "<"
        return this
    }

    infix fun lessEqualsThan(comparison: Any): Selector {
        this.comparison = comparison
        this.op = "<="
        return this
    }

    override fun toString(): String {
        return queryString + optionalAttribute()
    }

    override fun <T, R> accept(visitor: FormulaVisitor<T, R>, params: T): R {
        return visitor.visit(AtomicFormula(toString()), params)
    }

    private fun optionalAttribute(): String {
        return if (attribute != "") "\$$attribute $op $comparison" else ""
    }
}
