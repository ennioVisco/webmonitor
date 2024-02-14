package com.enniovisco.checking

import com.enniovisco.dsl.*
import com.enniovisco.space.*
import com.enniovisco.tracking.commands.*
import io.github.moonlightsuite.moonlight.offline.signal.*
import io.github.oshai.kotlinlogging.*
import kotlin.math.*


typealias Snapshot = Map<String, String>

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
    private val modifiers: MutableMap<String, (String, String) -> Boolean> =
        HashMap()
    private val log = KotlinLogging.logger {}

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
    fun useElements(elems: Map<String, (String, String) -> Boolean>) =
        apply { elems.forEach { useElement(it) } }

    /**
     * Method that allows to consider into a signal a given selected element
     * @param elem element id to select
     */
    fun useElement(elem: Map.Entry<String, (String, String) -> Boolean>) =
        apply { elements.add(elem.key); modifiers[elem.key] = elem.value }

    /**
     * Method that allows to consider into a signal a given selected element
     * @param elem element id to select, must not have a bound!
     */
    fun useElement(elem: String) =
        apply { elements.add(elem); modifiers[elem] = { _, _ -> true } }

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
        val snapshot = data[t]
        val boxes =
            elements.map { checkAtom(Atom(it), snapshot, loc = location) }
        return if (metadata)
            listOf(screenToBox(t).isContained(location)) + boxes
        else
            boxes
    }

    data class Atom(val fullName: String) {
        val selector: String
        val property: String
        val value: String
        private val log = KotlinLogging.logger {}

        init {
            try {
                val (selector, property, value) = parseSelector(fullName)
                this.selector = selector
                this.property = property
                this.value = value
            } catch (e: IllegalArgumentException) {
                val msg = "Unable to parse selector '$fullName'."
                log.error(msg)
                throw IllegalArgumentException(msg, e)
            }

        }

        fun comparator(): String {
            val allowedOnes = listOf(">=", "<=", "<<", ">>", "==", "@", "&")
            return when (val res = allowedOnes.find { fullName.contains(it) }) {
                null -> ""
                else -> res
            }
        }
    }

    private fun checkAtom(atom: Atom, snapshot: Snapshot, loc: Int): Boolean {
        val elements = dataToBoxes(atom.selector, snapshot)
        val matchingElements = matchingElems(elements, loc)

        return if (matchingElements.isNotEmpty() && atom.property != "") {
            compareMatchingElems(matchingElements, atom, snapshot)
        } else matchingElements.isNotEmpty()
    }

    private fun compareMatchingElems(
        matchingElems: List<Int>,
        atom: Atom,
        snapshot: Snapshot
    ): Boolean {
        return matchingElems.any { i ->
            val current = snapshot["${atom.selector}::$i::${atom.property}"]!!
            applyComparison(atom, current, snapshot)
        }
    }

    private fun matchingElems(elements: List<Box>, loc: Int): List<Int> {
        return elements.withIndex()
            .filter { (_: Int, element: Box) -> element.isContained(loc) }
            .map { it.index }
        //.reduce { acc, b -> acc || b }
    }

    private fun applyComparison(
        atom: Atom,
        value: String,
        snapshot: Map<String, String>
    ): Boolean {
        val op = atom.comparator()
        val comparison = atom.value
        val id = atom.fullName
        return when (op) {
            ">>" -> parsePixels(value) > parsePixels(comparison)
            ">=" -> parsePixels(value) >= parsePixels(comparison)
            "<<" -> parsePixels(value) < parsePixels(comparison)
            "<=" -> parsePixels(value) <= parsePixels(comparison)
            "==" -> value == comparison
            "&" -> sameAsBound(comparison, value, snapshot, mod(id))
            else -> true
        }
    }

    private fun mod(id: String) = modifiers[id] ?: { x, y -> x == y }

    private fun sameAsBound(
        bound: String,
        value: String,
        snapshot: Map<String, String>,
        modifier: (String, String) -> Boolean
    ): Boolean {
        val boundValue = snapshot["$BOUNDS_PREFIX$bound"]
        try {
            if (boundValue == null) {
                throw IllegalArgumentException("Bound '$bound' has value '${snapshot["$BOUNDS_PREFIX$bound"]}'.")
            }
            return modifier(value, boundValue)
        } catch (e: NullPointerException) {
            throw IllegalArgumentException("Bound '$bound' not found.")
        }
    }

    private fun Box.isContained(location: Int): Boolean {
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

    private fun dataToBoxes(id: String, snapshot: Snapshot): List<Box> {
        return try {
            val size: Int = snapshot["$id::size::"]!!.toInt()
            (0 until size).map { dataToBox(id, it, snapshot) }
        } catch (e: NullPointerException) {
//            log.warn { "Unable to find box coordinates for id: $id. Skipping." }
//            throw IllegalArgumentException(
//                "Unable to find box coordinates " +
//                        "for id: $id."
//            )
//            listOf(Box(0, 0, 0, 0))
            emptyList()
        }

    }

    private fun dataToBox(selector: String, i: Int, snapshot: Snapshot): Box {
        val minX: Int = parsePixels(snapshot["$selector::$i::x"]!!)
        val minY: Int = parsePixels(snapshot["$selector::$i::y"]!!)
        val maxX: Int = minX + parsePixels(snapshot["$selector::$i::width"]!!)
        val maxY: Int = minY + parsePixels(snapshot["$selector::$i::height"]!!)
        return Box(minX = minX, minY = minY, maxX = maxX, maxY = maxY)
    }

    private fun parsePixels(value: String): Int {
//        return if (value == "auto") {
//            0
//        } else {
        return value.replace("px", "").toDouble().roundToInt()
//        }
    }
}



