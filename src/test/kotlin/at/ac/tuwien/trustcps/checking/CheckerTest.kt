package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.function.BiFunction
import kotlin.test.assertEquals

internal class CheckerTest {

    @Disabled
    @Test fun `grid conforms to parameters`() {
        val width = 10
        val height = 20
        val mockData: List<Map<String, String>> = emptyList()
        val grid = Grid(height, width)
        val checker = Checker(grid, mockData)
        assertEquals(grid.rows, height)
        assertEquals(grid.columns, width)
    }

//    @Test fun `checking empty data fails`() {
//        val checker = Checker(2, 2, emptyList())
//        assertThrows<IllegalArgumentException>(
//            "Empty list should not be allowed")
//        {
//            checker.check(AtomicFormula("screen"))
//        }
//    }

    @Test fun `2d snapshot from signal`() {
        val grid = Grid(3, 3)
        val s: SpatialTemporalSignal<Boolean> =
            createStubSignal(grid.size, 1) { _, location ->
                location % 2 == 0
            }
        val snapshot = s.get2dSnapshot(grid, 0.0)

        for(i in 0 until s.size()) {
            val (x, y) = grid.toXY(i)
            assertEquals(s.signals[i].valueAt(0.0), snapshot[y][x])
        }
    }

    private fun <T> createStubSignal(locations: Int, end: Int,
                                     f: BiFunction<Double, Int, T>)
    : SpatialTemporalSignal<T> {
        val s = SpatialTemporalSignal<T>(locations)
        for(time in 0 .. end) {
            val dTime = time.toDouble()
            s.add(dTime) { f.apply(dTime, it) }
        }
        return s
    }
}