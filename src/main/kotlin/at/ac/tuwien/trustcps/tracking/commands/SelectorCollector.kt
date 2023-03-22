package at.ac.tuwien.trustcps.tracking.commands

import at.ac.tuwien.trustcps.dsl.*
import mu.*
import org.openqa.selenium.*

class SelectorCollector(
    queryString: String,
    cmdExec: (command: String) -> WebElement,
    private val rawCmdExec: (command: String) -> Any,
) : BrowserCommand(cmdExec) {
    private val selectorX: String
    private val selectorY: String
    private val selectorWidth: String
    private val selectorHeight: String
    private val propertyValue: String
    private val cssQuery: String
    private val cssProperty: String
    private val bound: BoundInitializer?
    private val log = KotlinLogging.logger {}

    init {
        val (query, property, label, isBinding) = parseSelector(queryString)
        cssQuery = query
        cssProperty = property
        val elem = cmdExec(query)

        // Rectangle class provides getX,getY, getWidth, getHeight methods
        selectorX = elem.rect.x.toString()
        selectorY = elem.rect.y.toString()
        selectorWidth = elem.rect.width.toString()
        selectorHeight = elem.rect.width.toString()

        log.info(
            "Element <${query}> = (${selectorX}, ${selectorY})" +
                    " -> (${selectorWidth}, ${selectorHeight})"
        )

        propertyValue = initializeProperty(property, elem)
        bound = initBound(isBinding, label)
    }

    private fun initializeProperty(property: String, elem: WebElement) =
        if (property != "") {
            val actual = elem.getCssValue(property)
            log.info("Property '$property' set at value: $actual")
            actual
        } else ""

    private fun initBound(isBinding: String, label: String) =
        if (isBinding == "true" && propertyValue != "")
            BoundInitializer(label, propertyValue, rawCmdExec)
        else null

    override fun dump(target: MutableMap<String, String>) {
        target["$cssQuery::x"] = selectorX
        target["$cssQuery::y"] = selectorY
        target["$cssQuery::width"] = selectorWidth
        target["$cssQuery::height"] = selectorHeight

        if (cssProperty != "") {
            target["$cssQuery::$cssProperty"] = propertyValue
        }

        bound?.dump(target)
    }

}
