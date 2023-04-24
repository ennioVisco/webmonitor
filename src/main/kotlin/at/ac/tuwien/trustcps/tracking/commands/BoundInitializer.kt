package at.ac.tuwien.trustcps.tracking.commands

import mu.*

const val BOUNDS_PREFIX = "wm-"

class BoundInitializer(
    private val label: String,
    initialValue: String,
    private val cmdExec: (String) -> Any
) : BrowserCommand() {
    private val value: String
    private val log = KotlinLogging.logger {}

    init {
        value = collectBound(label, initialValue)
    }

    private fun collectBound(bound: String, value: String): String {
        val actual = cmdExec(//language=JavaScript
            """return document.querySelector(':root').style.getPropertyValue(${
                prop(bound)
            })"""
        )
        return updateOrReturn(actual, bound, value)
    }

    private fun prop(bound: String) = "'--$BOUNDS_PREFIX$bound'"

    private fun updateOrReturn(
        actual: Any?,
        bound: String,
        defaultValue: String
    ): String {
        return if (actual == null || actual.toString() == "") {
            storeBoundValue(bound, defaultValue)
            defaultValue
        } else {
            log.warn("Bound '$bound' already set to '$actual'")
            return actual.toString()
        }
    }

    private fun storeBoundValue(bound: String, value: String) {
        cmdExec(//language=JavaScript
            "document.querySelector(':root').style.setProperty(${prop(bound)}, '$value')"
        )
        log.info("Bound '$bound' set to '$value'")
    }

    override fun dump(target: MutableMap<String, String>) {
        target["$BOUNDS_PREFIX$label"] = value
    }
}
