package at.ac.tuwien.trustcps.space

import eu.quanticol.moonlight.signal.GraphModel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GridTest {

    @Test
    fun rightCoordsFetch() {
        val grid = Grid(2, 3)
        val coords = Pair(2, 1)
        assertEquals(5, grid.toNode(coords))
    }

    @Test
    fun rightNodeFetch() {
        val grid = Grid(2, 3)
        val node = 5
        assertEquals(Pair(2, 1), grid.toXY(node))
    }

    @Test
    fun conversionIdempotence() {
        val grid = Grid(2, 3)
        val coords = Pair(2, 1)
        val node = 5
        assertEquals(coords, grid.toXY(grid.toNode(coords)))
        assertEquals(node, grid.toNode(grid.toXY(node)))
    }

    @Test
    fun correctSize() {
        val grid = Grid(2, 3)
        assertEquals(2, grid.rows)
        assertEquals(3, grid.columns)
        assertEquals(6, grid.size)
    }

    @Test
    fun test3() {
        val model = Grid(3, 3).model
        val stub = stubGrid()
        assertEquals(model.size(), stub.size())
        for (i in 0 until model.size()) {
            for (j in 0 until model.size()) {
                // println("($i, $j)");
                assertEquals(stub[i, j], model[i, j])
            }
        }
    }

    private fun stubGrid(): GraphModel<Int> {
        val model = GraphModel<Int>(9)

        // 0
        model.add(0, 1, 1)
        model.add(0, 1, 3)
        // 1
        model.add(1, 1, 0)
        model.add(1, 1, 2)
        model.add(1, 1, 4)
        // 2
        model.add(2, 1, 1)
        model.add(2, 1, 5)
        // 3
        model.add(3, 1, 0)
        model.add(3, 1, 4)
        model.add(3, 1, 6)
        // 4
        model.add(4, 1, 1)
        model.add(4, 1, 3)
        model.add(4, 1, 5)
        model.add(4, 1, 7)
        // 5
        model.add(5, 1, 2)
        model.add(5, 1, 4)
        model.add(5, 1, 8)
        // 6
        model.add(6, 1, 3)
        model.add(6, 1, 7)
        // 7
        model.add(7, 1, 4)
        model.add(7, 1, 6)
        model.add(7, 1, 8)
        // 8
        model.add(8, 1, 5)
        model.add(8, 1, 7)
        return model
    }
}