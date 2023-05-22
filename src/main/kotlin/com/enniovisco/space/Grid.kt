package com.enniovisco.space

import eu.quanticol.moonlight.core.space.*
import eu.quanticol.moonlight.space.*

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

    private fun generateModel(rows: Int, columns: Int) =
        RegularGridModel(rows, columns, 1)

    /**
     * from 1-d location to (X,Y) pairs
     */
    fun toXY(location: Int): Pair<Int, Int> {
        val coords = model.toCoordinates(location)
        return Pair(coords[0], coords[1])
    }

    /**
     * from (X,Y) pairs to 1-d location
     */
    fun toNode(xyCoords: Pair<Int, Int>): Int {
        return model.fromCoordinates(xyCoords.first, xyCoords.second)
    }

    /**
     * It calculates the proper distance, given a spatial model
     *
     * @return a DoubleDistance object, meaningful in the given Spatial Model
     */
    fun distance(max: Int = size): DistanceStructure<Int, *> {
        val isParallel = true
        val baseline = 0
        return IntManhattanDistanceStructure(isParallel, baseline, max, model)
    }
}
