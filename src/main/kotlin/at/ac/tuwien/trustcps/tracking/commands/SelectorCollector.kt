package at.ac.tuwien.trustcps.tracking.commands

import at.ac.tuwien.trustcps.dsl.*
import org.openqa.selenium.*

class SelectorCollector(
    queryString: String,
    commandExecutor: (command: String) -> WebElement
) :
    BrowserCommand(commandExecutor) {

    private val selectorX: String
    private val selectorY: String
    private val selectorWidth: String
    private val selectorHeight: String
    private val selectorProperty: String
    private val cssQuery: String
    private val cssProperty: String

    init {
        val (cssQuery, cssProperty, _) = parseSelector(queryString)
        this.cssQuery = cssQuery
        this.cssProperty = cssProperty
        val elem = commandExecutor(cssQuery)

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        selectorX = elem.rect.x.toString()
        selectorY = elem.rect.y.toString()
        selectorWidth = elem.rect.width.toString()
        selectorHeight = elem.rect.width.toString()

        println(
            "Element <${cssQuery}> = " +
                    "(${selectorX}, ${selectorY})" +
                    " -> " +
                    "(${selectorWidth}, ${selectorHeight})"
        )

        if (cssProperty != "") {
            selectorProperty = elem.getCssValue(cssProperty)
            println("Property '$cssProperty' value: $selectorProperty")
        } else {
            selectorProperty = ""
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
    }

}
