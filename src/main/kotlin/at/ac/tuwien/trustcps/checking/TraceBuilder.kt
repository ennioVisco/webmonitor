package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.dsl.*
import at.ac.tuwien.trustcps.space.*
import at.ac.tuwien.trustcps.tracking.commands.*
import eu.quanticol.moonlight.offline.signal.*
import kotlin.math.*

/**
 *  Builder class that generates a signal based on the data
 *  @param grid spatial model to consider for building the signal
 *  @param data data source from which the signal will be extracted
 */
class TraceBuilder(
    private val grid: Grid,
    private val data: List<Map<String, String>>
) {
    private var metadata: Boolean = false
    private val elements: MutableList<String> = ArrayList()

    /**
     * Modifier determining whether to also load page metadata in the signal
     */
    fun useMetadata() = apply { metadata = true }

    /**
     * Method to determine whether also metadata is/will be loaded in the signal
     */
    fun hasMetadata() = metadata

    /**
     * Method that allows to consider into a signal a list of selected elements
     * @param elems list of element ids to select
     */
    fun useElements(elems: List<String>) =
        apply { elems.forEach { useElement(it) } }

    /**
     * Method that allows to consider into a signal a given selected element
     * @param elem element id to select
     */
    fun useElement(elem: String) = apply { elements.add(elem) }

    /**
     * Method that allows to determine whether a given selected element
     * has/will be loaded into the signal
     * @param elem element id to select
     */
    fun hasElement(elem: String) = elements.contains(elem)

    /**
     * Method that completes and returns the building of a
     * [SpatialTemporalSignal] for the given page trace
     */
    fun build(): SpatialTemporalSignal<List<Boolean>> {
        val signal = SpatialTemporalSignal<List<Boolean>>(grid.size)
        for (t in data.indices) {
            signal.add(t.toDouble()) { location -> activeElements(t, location) }
        }
        return signal
    }

    private fun activeElements(t: Int, location: Int): List<Boolean> {
        val boxes = elements.map { checkAtom(it, t = t, location = location) }
        return if (metadata)
            listOf(screenToBox(t).has(location)) + boxes
        else
            boxes
    }

    private fun checkAtom(atom: String, t: Int, location: Int): Boolean {
        val (selector, property, value) = parseSelector(atom)
        val isPresent = dataToBox(selector, t).has(location)
        val op = getComparator(atom)
        if (property != "") {
            val eval = applyComparison(op, selector, property, value, data[t])
            return isPresent && eval
        }
        return isPresent
    }

    private fun getComparator(atom: String): String {
        val allowedComparators = listOf(">=", "<=", "<", ">", "=", "@", "&")
        return when (val res = allowedComparators.find { atom.contains(it) }) {
            null -> ""
            else -> res
        }
    }

    private fun applyComparison(
        op: String,
        selector: String,
        property: String,
        comparison: String,
        snapshot: Map<String, String>
    ): Boolean {
        val value = snapshot["$selector::${property}"]!!
        return when (op) {
            ">" -> parsePixels(value) > parsePixels(comparison)
            ">=" -> parsePixels(value) >= parsePixels(comparison)
            "<" -> parsePixels(value) < parsePixels(comparison)
            "<=" -> parsePixels(value) <= parsePixels(comparison)
            "=" -> value == comparison
            "&" -> sameAsBound(comparison, value, snapshot)
            else -> true
        }
    }

    private fun sameAsBound(
        bound: String,
        value: String,
        snapshot: Map<String, String>
    ): Boolean {
        val boundValue = snapshot["$BOUNDS_PREFIX$bound"]
        try {
            if (boundValue == null) {
                throw IllegalArgumentException("Bound '$bound' has value '${snapshot["$BOUNDS_PREFIX$bound"]}'.")
            }
            return value == boundValue
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("Bound '$bound' not found.")
        }
    }

    private fun Box.has(location: Int): Boolean {
        return contains(grid.toXY(location))
    }

    private fun screenToBox(time: Int): Box {
        try {
            val maxX = data[time]["vvp_width"]!!
            val maxY = data[time]["vvp_height"]!!
            return Box.from(minX = "0", minY = "0", maxX = maxX, maxY = maxY)
        } catch (e: NullPointerException) {
            throw IllegalArgumentException(
                "Unable to find box coordinates for the window."
            )
        }
    }

    private fun dataToBox(id: String, index: Int): Box {
        try {
            val minX: Int = parsePixels(data[index]["$id::x"]!!)
            val minY: Int = parsePixels(data[index]["$id::y"]!!)
            val maxX: Int = minX + parsePixels(data[index]["$id::width"]!!)
            val maxY: Int = minY + parsePixels(data[index]["$id::height"]!!)
            return Box(minX = minX, minY = minY, maxX = maxX, maxY = maxY)
        } catch (e: NullPointerException) {
            throw IllegalArgumentException(
                "Unable to find box coordinates " +
                        "for id: $id."
            )
        }

    }

    private fun parsePixels(value: String): Int {
//        return if (value == "auto") {
//            0
//        } else {
        return value.replace("px", "").toDouble().roundToInt()
//        }
    }
}
