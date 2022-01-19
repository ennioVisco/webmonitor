
import eu.quanticol.moonlight.signal.GraphModel
import org.junit.jupiter.api.Test
import space.Grid
import kotlin.test.assertEquals

class GridTest {

    @Test
    fun test3() {
        val grid = Grid.getModel(3, 3)
        val stub = stubGrid()
        assertEquals(grid.size(), stub.size())
        for (i in 0..8) {
            for (j in 0..8) {
                //System.out.println("("+ i + ", " + j + ")");
                val left = grid[i, j]
                val right = stub[i, j]
                assertEquals(left, right)
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