package at.ac.tuwien.trustcps.space

import eu.quanticol.moonlight.signal.DistanceStructure
import eu.quanticol.moonlight.signal.GraphModel
import eu.quanticol.moonlight.signal.SpatialModel

class Grid(val rows: Int, val columns: Int) {
    /**
     * The spatial model built for the current grid
     * @return a [GraphModel]
     * @see SpatialModel
     */
    val model = generateModel(rows, columns)
    /**
     * The 1-d size of the grid
     * @return [rows] x [columns]
     */
    val size = rows * columns

    private fun generateModel(rows: Int, columns: Int): GraphModel<Int> {
        val size = rows * columns
        val model = GraphModel<Int>(size)
        for (i in 0 until size) {
            val ns = getNeighboursArray(i, size)
            for (neighbour in ns) {
                model.add(i, 1, neighbour)
            }
        }
        return model
    }

    private fun getNeighboursArray(node: Int, size: Int)
    : List<Int>
    {
        val neighbours: MutableList<Int> = ArrayDeque(4)

        if (node + columns < size)                 // bot boundary
            neighbours.add(node + columns)

        if (node - columns >= 0)                   // top boundary
            neighbours.add(node - columns)

        if (node % columns == 0) {                 // left border
            neighbours.add(node + 1)

        } else if (node % columns == columns - 1) {   // right border
            neighbours.add(node - 1)

        } else {                                // others
            neighbours.add(node - 1)
            neighbours.add(node + 1)
        }

        return neighbours
    }

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
    fun distance(): DistanceStructure<Int, *> {
        return  DistanceStructure(
                { x: Int -> x },
                IntegerDistance(),
                0, size,
                model
            )
    }
}
