package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.Grid
import eu.quanticol.moonlight.formula.AtomicFormula
import eu.quanticol.moonlight.signal.SpatialTemporalSignal
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.function.BiFunction
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class CheckerTest {

//    @Test fun calculateAddsValues() {
//        val mockData = mockk<List<Map<String, String>>>()
//
//        val slot = slot<SpatialTemporalSignal<List<Boolean>>>()
//        val mock = mockk<Checker>()
//        every { mockData[0] } returns 1
//
//        every { mockData } returns "test"
//        every { doc2.value2 } returns "6"
//
//        val sut = Checker(doc1, doc2)
//
//        assertEquals(11, checker.check(AtomicFormula("true")))
//    }

    @Disabled
    @Test fun `checking empty data fails`() {
        val grid = Grid(2, 2)
        val checker = Checker(grid, emptyList(), emptyList())
        assertFailsWith<IllegalArgumentException>(
            "Empty data in input should not be allowed") {
            checker.check(AtomicFormula("screen"))
        }
    }

    @Test fun `2d snapshot from signal`() {
        val grid = Grid(3, 3)
        val s = evenLocationsAreTrueSignal(grid.size)
        val snapshot = s.get2dSnapshot(grid, 0.0)

        for(i in 0 until s.size()) {
            val (x, y) = grid.toXY(i)
            assertEquals(s.signals[i].valueAt(0.0), snapshot[y][x])
        }
    }

    private fun evenLocationsAreTrueSignal(size: Int) =
        createStubSignal(size) { _, location ->
        location % 2 == 0
    }

    private fun <T> createStubSignal(locations: Int,
                                     f: BiFunction<Double, Int, T>)
    : SpatialTemporalSignal<T> {
        val s = SpatialTemporalSignal<T>(locations)
        for(time in 0 .. 1) {
            val dTime = time.toDouble()
            s.add(dTime) { f.apply(dTime, it) }
        }
        return s
    }
}