package at.ac.tuwien.trustcps.space

import eu.quanticol.moonlight.core.space.DistanceStructure
import eu.quanticol.moonlight.domain.IntegerDomain
import eu.quanticol.moonlight.space.ManhattanDistanceStructure
import eu.quanticol.moonlight.space.RegularGridModel

class Grid(val rows: Int, val columns: Int) {
    /**
     * The spatial model built for the current grid
     */
    val model = generateModel(rows, columns)
    /**
     * The 1-d size of the grid
     * @return [rows] x [columns]
     */
    val size = model.size()

    private fun generateModel(rows: Int, columns: Int)
        = RegularGridModel(rows, columns, 1)

    /**
     * from 1-d location to (X,Y) pairs
     */
    fun toXY(location: Int): Pair<Int, Int> {
        return fromArray(location)
    }

    /**
     * from (X,Y) pairs to 1-d location
     */
    fun toNode(xyCoords: Pair<Int, Int>): Int {
        return toArray(xyCoords.first, xyCoords.second)
    }

    /**
     * Given a pair of coordinates of a node,
     * it returns their array-style version
     * @param columnIndex first coordinate of the node
     * @param rowIndex second coordinate of the node
     * @return an int corresponding to the serialized coordinates.
     */
    private fun toArray(columnIndex: Int, rowIndex: Int): Int {
        return columnIndex + rowIndex * columns
    }

    /**
     * Given the dimension n of the square matrix, it converts a position in
     * the array to a position in the n x n matrix.
     * @param a position in the array
     * @return the pair (i,j) of coordinates in the matrix.
     */
    private fun fromArray(a: Int): Pair<Int, Int> {
        val column = a % columns
        val row = a / columns
        return Pair(column, row)
    }

    /**
     * It calculates the proper distance, given a spatial model
     *
     * @return a DoubleDistance object, meaningful in the given Spatial Model
     */
    fun distance(max: Int = size): DistanceStructure<Int, *> {
        return  ManhattanDistanceStructure(
                { x: Int -> x },
                IntegerDomain(),
                0, max,
                model
            )
    }
}
