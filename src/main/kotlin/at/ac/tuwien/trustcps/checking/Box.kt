package at.ac.tuwien.trustcps.checking

/**
 * Data class that denotes a box surrounding an element placed in the page
 * @param minX = minimum coordinate over the X axis
 * @param minY = minimum coordinate over the Y axis
 * @param maxX = maximum coordinate over the X axis
 * @param maxY = maximum coordinate over the Y axis
 */
data class Box(
    val minX: Int, val minY: Int,
    val maxX: Int, val maxY: Int
) {
    init {
        require(minX < maxX && minY < maxY) {
            "Box borders must be proper intervals, they were x: " +
                    "[${minX}, ${maxX}], and y: [${minY}, ${maxY}]"
        }
    }

    /**
     * Given a pair of coordinates (X, Y) it tells whether it is contained in
     * the current box or not.
     * @param coords the coordinates to be checked.
     */
    fun contains(coords: Pair<Int, Int>): Boolean {
        return contains(coords.first, coords.second)
    }

    /**
     * Given coordinates X and Y, it tells whether the couple is contained in
     * the current box or not.
     * @param x the coordinates over the X axis to be checked.
     * @param y the coordinates over the Y axis to be checked.
     */
    fun contains(x: Int, y: Int): Boolean {
        return x in minX..maxX
                && y in minY..maxY
    }

    companion object {
        /**
         * TODO: remove, too dangerous: if people sum string it doesn't get caught
         * Factory method that generates a [Box], given strings representing
         * the coordinates of the box.
         * @param minX = minimum coordinate over the X axis
         * @param minY = minimum coordinate over the Y axis
         * @param maxX = maximum coordinate over the X axis
         * @param maxY = maximum coordinate over the Y axis
         */
        fun from(minX: String, minY: String, maxX: String, maxY: String) =
            Box(minX.toInt(), minY.toInt(), maxX.toInt(), maxY.toInt())
    }
}