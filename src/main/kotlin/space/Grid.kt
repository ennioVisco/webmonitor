package space

import eu.quanticol.moonlight.signal.DistanceStructure
import eu.quanticol.moonlight.signal.GraphModel
import eu.quanticol.moonlight.signal.SpatialModel
import java.util.function.Function

object Grid {
    fun getModel(rows: Int, columns: Int): GraphModel<Int> {
        val size = rows * columns
        val model = GraphModel<Int>(size)
        for (i in 0 until size) {
            val ns = getNeighbours(i, size, columns)
            for (neighbour in ns) {
                model.add(i, 1, neighbour)
            }
        }
        return model
    }

    fun getNeighbours(node: Int, size: Int, cols: Int): List<Int> {
        val neighbours: MutableList<Int> = ArrayDeque(4)

        // bot boundary
        if (node + cols < size) neighbours.add(node + cols)

        // top boundary
        if (node - cols >= 0) neighbours.add(node - cols)

        // left border
        if (node % cols == 0) {
            neighbours.add(node + 1)
            // right border
        } else if (node % cols == cols - 1) {
            neighbours.add(node - 1)
            // others
        } else {
            neighbours.add(node - 1)
            neighbours.add(node + 1)
        }

        return neighbours
    }

    /**
     * Given a pair of coordinates of a node,
     * it returns their array-style version
     * @param i first coordinate of the node
     * @param j second coordinate of the node
     * @param m the number of columns of the matrix
     * @return an int corresponding to the serialized coordinates.
     */
    private fun toArray(i: Int, j: Int, m: Int): Int {
        return i + j * m
    }

    /**
     * Given the dimension n of the square matrix, it converts a position in
     * the array to a position in the n x n matrix.
     * @param a position in the array
     * @param m the number of columns of the matrix
     * @return the pair (i,j) of coordinates in the matrix.
     */
    private fun fromArray(a: Int, m: Int): Pair<Int, Int> {
        val i = a % m
        val j = a / m
        return Pair(i, j)
    }

    /**
     * It calculates the proper distance, given a spatial model
     *
     * @param lowerBound double representing the starting position
     * @param upperBound double representing the ending position
     * @return a DoubleDistance object, meaningful in the given Spatial Model
     */
    fun distance(lowerBound: Int, upperBound: Int):
            Function<SpatialModel<Int>, DistanceStructure<Int, *>>
    {
        return Function { g: SpatialModel<Int>? ->
            DistanceStructure(
                { x: Int? -> x },
                IntegerDistance(),
                lowerBound, upperBound,
                g
            )
        }
    }
}
