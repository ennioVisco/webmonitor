package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.parseSelector
import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.SpatialTemporalSignal

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
        val (selector, property, comparator) = parseSelector(atom)
        val isPresent = dataToBox(selector, t).has(location)
        val op = getOperator(atom)
        val eval = select(op, selector, property, comparator, t)
        return isPresent && eval
    }

    private fun getOperator(atom: String): String {
        return if (atom.contains(">=")) {
            ">="
        } else if (atom.contains('>')) {
            ">"
        } else if (atom.contains("<=")) {
            "<="
        } else if (atom.contains('<')) {
            "<"
        } else if (atom.contains('=')) {
            "="
        } else {
            ""
        }
    }

    private fun select(
        op: String,
        selector: String, property: String, comparator: String,
        t: Int
    ): Boolean {
        if (property != "") {
            val value = data[t]["$selector::${property}"]!!
            return when (op) {
                ">" -> value.toDouble() > comparator.toDouble()
                ">=" -> value.toDouble() >= comparator.toDouble()
                "<" -> value.toDouble() < comparator.toDouble()
                "<=" -> value.toDouble() <= comparator.toDouble()
                "=" -> value == comparator
                "" -> data[t]["$selector::${property}"] == value
                else -> error("Unsupported operator in atom definition")
            }
        }
        return true
    }

    private fun Box.has(location: Int): Boolean {
        return this.contains(grid.toXY(location))
    }

    private fun screenToBox(time: Int): Box {
        try {
            val maxX = data[time]["vvp_width"]!!
            val maxY = data[time]["vvp_height"]!!
            return Box.from(minX = "0", minY = "0", maxX = maxX, maxY = maxY)
        } catch (e: NullPointerException) {
            throw IllegalArgumentException(
                "Unable to find box coordinates " +
                        "for the window."
            )
        }
    }

    private fun dataToBox(id: String, index: Int): Box {
        try {
            val minX: Int = data[index]["$id::x"]!!.toInt()
            val minY: Int = data[index]["$id::y"]!!.toInt()
            val maxX: Int = minX + data[index]["$id::width"]!!.toInt()
            val maxY: Int = minY + data[index]["$id::height"]!!.toInt()
            return Box(minX = minX, minY = minY, maxX = maxX, maxY = maxY)
        } catch (e: NullPointerException) {
            throw IllegalArgumentException(
                "Unable to find box coordinates " +
                        "for id: $id."
            )
        }

    }
}
