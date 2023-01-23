package at.ac.tuwien.trustcps.tracking.commands

import at.ac.tuwien.trustcps.dsl.*
import org.openqa.selenium.*

class SelectorCollector(
    queryString: String,
    cmdExec: (command: String) -> WebElement,
    private val rawCmdExec: (command: String) -> Any,
) :
    BrowserCommand(cmdExec) {
    private val selectorX: String
    private val selectorY: String
    private val selectorWidth: String
    private val selectorHeight: String
    private val selectorProperty: String
    private val cssQuery: String
    private val cssProperty: String
    private val boundValue: String

    init {
        val (query, property, value, isBinding) = parseSelector(queryString)
        cssQuery = query
        cssProperty = property
        val elem = cmdExec(query)

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        selectorX = elem.rect.x.toString()
        selectorY = elem.rect.y.toString()
        selectorWidth = elem.rect.width.toString()
        selectorHeight = elem.rect.width.toString()

        println(
            "Element <${query}> = (${selectorX}, ${selectorY})" +
                    " -> (${selectorWidth}, ${selectorHeight})"
        )

        selectorProperty = initializeProperty(property, elem)
        boundValue = initializeBound(isBinding, selectorProperty, value)
    }

    private fun initializeBound(
        isBinding: String,
        currentValue: String,
        bound: String
    ): String {
        return if (isBinding == "true") {
            val actual =
                rawCmdExec(
                    "return document.querySelector(':root').style.getPropertyValue(${
                        prop(
                            bound
                        )
                    })"
                )
            updateOrReturn(actual, currentValue, bound)
        } else {
            ""
        }
    }

    private fun prop(bound: String) = "'--$BOUNDS_PREFIX$bound'"

    private fun updateOrReturn(
        actual: Any?,
        currentValue: String,
        bound: String
    ): String {
        return if (actual == null || actual.toString() == "") {
            rawCmdExec(
                "document.querySelector(':root').style.setProperty(${
                    prop(bound)
                }, '$currentValue')"
            )
            println("Bound '$bound' set to '$currentValue'")
            currentValue
        } else {
            println("Bound '$bound' already set to '$actual'")
            actual.toString()
        }
    }

    private fun initializeProperty(property: String, elem: WebElement): String {
        return if (property != "") {
            val actual = elem.getCssValue(property)
            println("Property '$property' value: $actual")
            actual
        } else {
            ""
        }
    }

    override fun dump(target: MutableMap<String, String>) {
        target["$cssQuery::x"] = selectorX
        target["$cssQuery::y"] = selectorY
        target["$cssQuery::width"] = selectorWidth
        target["$cssQuery::height"] = selectorHeight
        if (cssProperty != "") {
            target["$cssQuery::$cssProperty"] = selectorProperty
        }

        if (boundValue != "") {
            target["$BOUNDS_PREFIX$boundValue"] = "true"
        }
    }

}
