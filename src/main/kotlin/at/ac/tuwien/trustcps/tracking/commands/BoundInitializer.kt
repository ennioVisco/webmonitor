package at.ac.tuwien.trustcps.tracking.commands

const val BOUNDS_PREFIX = "wm-"

class BoundInitializer(
    private val label: String,
    initialValue: String,
    cmdExec: (String) -> Any
) : BrowserCommand(cmdExec) {
    private val value: String

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
            println("Bound '$bound' already set to '$actual'")
            return actual.toString()
        }
    }

    private fun storeBoundValue(bound: String, value: String) {
        cmdExec(//language=JavaScript
            "document.querySelector(':root').style.setProperty(${prop(bound)}, '$value')"
        )
        println("Bound '$bound' set to '$value'")
    }

    override fun dump(target: MutableMap<String, String>) {
        target["$BOUNDS_PREFIX$label"] = value
    }
}
