package at.ac.tuwien.trustcps.dsl

import eu.quanticol.moonlight.formula.*

/**
 * Pseudo-selector for selecting the whole document
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
    private val op: String = "",
    val modifier: (current: String, bound: String) -> Boolean = ::equality
) : AtomicFormula(stringify(queryString, attribute, op, comparison)) {

    infix fun read(attribute: String): Selector {
        return Selector(queryString, attribute)
    }

    infix fun at(state: String): Selector {
        // TODO: not sure it is currently working!
        return Selector(queryString, state = state)
    }

    infix fun equalTo(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, "=")
    }

    infix fun bind(label: String): Selector {
        return Selector(queryString, attribute, label, state, "&")
    }

    infix fun applying(modifier: (current: String, bound: String) -> Boolean) =
        Selector(
            queryString,
            attribute,
            comparison,
            state,
            "&",
            modifier
        )

    infix fun greaterThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, ">")
    }

    infix fun greaterEqualThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, ">=")
    }

    infix fun lessThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, "<")
    }

    infix fun lessEqualThan(comparison: Any): Selector {
        return Selector(queryString, attribute, comparison, state, "<=")
    }

    override fun toString(): String {
        return stringify(queryString, attribute, op, comparison)
    }

    companion object {
        @JvmStatic
        private fun stringify(
            queryString: String,
            attribute: String,
            op: String,
            comparison: Any?
        ) = queryString + optionalAttribute(attribute, op, comparison)

        @JvmStatic
        private fun optionalAttribute(
            attribute: String,
            op: String,
            comparison: Any?
        ): String {
            return if (attribute != "" && op != "" && comparison != null) {
                "\$$attribute $op $comparison"
            } else ""
        }

        @JvmStatic
        private fun equality(current: String, bound: String) = current == bound
    }
}
