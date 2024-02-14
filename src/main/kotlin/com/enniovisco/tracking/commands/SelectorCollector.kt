package com.enniovisco.tracking.commands

import com.enniovisco.dsl.*
import io.github.oshai.kotlinlogging.*
import org.openqa.selenium.*

class SelectorCollector(
    queryString: String,
    cmdExec: (command: String) -> List<WebElement>,
    private val rawCmdExec: (command: String) -> Any,
) : BrowserCommand() {
    private val selectorX = mutableListOf<String>()
    private val selectorY = mutableListOf<String>()
    private val selectorWidth = mutableListOf<String>()
    private val selectorHeight = mutableListOf<String>()
    private val propertyValue: String
    private val cssQuery: String
    private val cssProperty: String
    private val bound: BoundInitializer?
    private val log = KotlinLogging.logger {}

    init {
        log.info("SelectorCollector: $queryString")
        val (query, property, label, isBinding) = parseSelector(queryString)
        cssQuery = query
        cssProperty = property

        val elems = cmdExec(query)

        collectElements(elems)

        propertyValue = initializeProperty(property, elems[0])
        bound = initBound(isBinding, label)
    }

    private fun collectElements(elements: List<WebElement>) {
        for ((i, elem) in elements.withIndex()) {
            // Rectangle class provides getX,getY, getWidth, getHeight methods
            selectorX.add(elem.rect.x.toString())
            selectorY.add(elem.rect.y.toString())
            selectorWidth.add(elem.rect.width.toString())
            selectorHeight.add(elem.rect.height.toString())

            log.info("[PERF] Element <$cssQuery> size: ${elements.size}")

            if (elements.size < 6) {
                log.info(
                    "Element <$cssQuery>[$i] =" +
                            " (${selectorX[i].toInt()}, ${selectorY[i].toInt()})" +
                            " -> (${selectorX[i].toInt() + selectorWidth[i].toInt()}" +
                            ", ${selectorY[i].toInt() + selectorHeight[i].toInt()})"
                )
            }
        }
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
        target["$cssQuery::size::"] = selectorX.size.toString()
        for (i in 0 until selectorX.size) {
            target["$cssQuery::$i::x"] = selectorX[i]
            target["$cssQuery::$i::y"] = selectorY[i]
            target["$cssQuery::$i::width"] = selectorWidth[i]
            target["$cssQuery::$i::height"] = selectorHeight[i]

            if (cssProperty != "") {
                target["$cssQuery::$i::$cssProperty"] = propertyValue
            }
        }

        bound?.dump(target)
    }

}
