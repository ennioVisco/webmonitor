package at.ac.tuwien.trustcps.checking

import at.ac.tuwien.trustcps.space.*
import eu.quanticol.moonlight.offline.signal.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class TraceBuilderTest {
    @Test
    fun `malformed data makes building fail`() {
        val builder = builderMalformedData()
        builder.useElement("elem")

        assertFailsWith<IllegalArgumentException> { builder.build() }
    }

    @Test
    fun `malformed metadata makes building fail`() {
        val builder = builderMalformedData()
        builder.useMetadata()

        assertFailsWith<IllegalArgumentException> { builder.build() }
    }

    @Test
    fun `simple builder test`() {
        val stub = toArray(signalStub())

        val built = toArray(builderOneElementInit().build())

        assertArrayEquals(stub, built)
    }

    @Test
    fun `simple builder metadata test`() {
        val stub = toArray(alwaysTrueSignal())

        val built = toArray(builderScreenInit().build())

        assertArrayEquals(stub, built)
    }

    @Nested
    inner class Modifiers {
        @Test
        fun `metadata modifier actually enables metadata`() {
            val builder = builderScreenInit()

            builder.useMetadata()

            assertTrue(builder.hasMetadata())
        }

        @Test
        fun `elements modifier actually adds elements`() {
            val builder = builderScreenInit()

            builder.useElements(listOf("element1", "element2"))

            assertTrue(builder.hasElement("element1"))
            assertTrue(builder.hasElement("element2"))
        }

        @Test
        fun `element modifier actually adds an element`() {
            val builder = builderScreenInit()

            builder.useElement("element1")

            assertTrue(builder.hasElement("element1"))
        }
    }

    private fun signalStub(): SpatialTemporalSignal<List<Boolean>> {
        val signal = SpatialTemporalSignal<List<Boolean>>(9)
        signal.add(0.0) {
            if (it in listOf(0, 1, 3, 4)) {     //  1 1 0
                listOf(true)                    //  1 1 0
            } else {                            //  0 0 0
                listOf(false)
            }
        }
        return signal
    }

    private fun alwaysTrueSignal(): SpatialTemporalSignal<List<Boolean>> {
        val signal = SpatialTemporalSignal<List<Boolean>>(9)
        signal.add(0.0) { listOf(true) }
        return signal
    }

    private fun toArray(signal: SpatialTemporalSignal<List<Boolean>>) =
        signal.toArray {
            when (it[0]) {
                true -> 1.0
                else -> 0.0
            }
        }

    private fun builderMalformedData(): TraceBuilder {
        val grid = Grid(3, 3)
        val data = listOf(
            mapOf(
                "vvp_width" to "2",
                "elem::y" to "2"
            )
        )
        val builder = TraceBuilder(grid, data)
        builder.useMetadata()
        return builder
    }

    private fun builderScreenInit(): TraceBuilder {
        val grid = Grid(3, 3)
        val data = listOf(
            mapOf(
                "vvp_width" to "2",
                "vvp_height" to "2"
            )
        )
        val builder = TraceBuilder(grid, data)
        builder.useMetadata()
        return builder
    }

    private fun builderOneElementInit(): TraceBuilder {
        val grid = Grid(3, 3)
        val data = listOf(
            mapOf(
                "elem::x" to "0",
                "elem::y" to "0",
                "elem::width" to "1",
                "elem::height" to "1"
            )
        )
        val builder = TraceBuilder(grid, data)
        builder.useElement("elem")
        return builder
    }
}
