package at.ac.tuwien.trustcps.dsl

import eu.quanticol.moonlight.core.formula.Formula
import eu.quanticol.moonlight.core.formula.FormulaVisitor
import eu.quanticol.moonlight.formula.AtomicFormula

/**
 * pseudo-selector for selecting the whole document 
 */
internal val document = Selector("document")

/**
 * Class including all the info for CSS selectors definition
 */
data class Selector(
    val queryString: String,
    val attribute: String = "",
    val comparison: Any? = null,
    val state: String = "",
    private val op: String = ""
) : Formula {

    infix fun read(attribute: String): Selector {
        return Selector(queryString, attribute)
    }
    
    infix fun at(state: String): Selector {
        return Selector(queryString, state = state)
    }

    @Suppress("CovariantEquals")
    infix fun equals(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, "=")
    }

    infix fun greaterThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, ">")
    }

    infix fun greaterEqualsThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, ">=")
    }

    infix fun lessThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, "<")
    }

    infix fun lessEqualsThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, "<=")
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
