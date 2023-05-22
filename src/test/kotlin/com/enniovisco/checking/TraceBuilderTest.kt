package com.enniovisco.checking

import com.enniovisco.*
import com.enniovisco.space.*
import eu.quanticol.moonlight.offline.signal.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.*

internal class TraceBuilderTest {

    private val dummyElems = mapOf(
        "elem" to { _: String, _: String -> true },
        "elem2" to { _: String, _: String -> true }
    )

    @Test
    fun `malformed data makes building fail`() {
        val builder = builderMalformedData()

        builder.useElement(dummyElems.entries.first())

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
        val builder = builderOneElementInit()

        builder.useElement(dummyElems.entries.first())
        val built = builder.build()

        assertSignalDimensionEquals(signalStub, built)
    }

    @Test
    fun `simple builder metadata test`() {
        val built = builderMetadataInit().build()
        assertSignalDimensionEquals(alwaysTrueSignal, built)
    }

    @Test
    fun `complex signal builder test`() {
        val built =
            builderOneElementFlashingInit().useElements(dummyElems).build()
        assertSignalDimensionEquals(alwaysTrueThenAlwaysFalse, built)
    }

    @Nested
    inner class Modifiers {
        @Test
        fun `metadata modifier actually enables metadata`() {
            val builder = builderMetadataInit()

            builder.useMetadata()

            assertTrue(builder.hasMetadata())
        }

        @Test
        fun `elements modifier actually adds elements`() {
            val builder = builderMetadataInit()

            builder.useElements(dummyElems)

            assertTrue(builder.hasElement("elem"))
            assertTrue(builder.hasElement("elem2"))
        }

        @Test
        fun `element modifier actually adds an element`() {
            val builder = builderMetadataInit()

            builder.useElement(dummyElems.entries.first())

            assertTrue(builder.hasElement("elem"))
        }
    }

    @Nested
    inner class Operators {
        @Test
        fun ` 'bigger than' works`() {
            val builder = builderOneElementInit()

            builder.useElement("elem\$width >> 3")
            val builtSignal = builder.build()

            assertSignalDimensionEquals(alwaysFalseSignal, builtSignal)
        }

        @Test
        fun ` 'equal to' works`() {
            val builder = builderOneElementInit()

            builder.useElement("elem\$width == 1")
            val builtSignal = builder.build()

            assertSignalDimensionEquals(signalStub, builtSignal)
        }

        @Test
        fun ` 'bigger than or equal to' works`() {
            val builder = builderOneElementInit()

            builder.useElement("elem\$width >= 1")
            val builtSignal = builder.build()

            assertSignalDimensionEquals(signalStub, builtSignal)
        }

        @Test
        fun ` 'less than' works`() {
            val builder = builderOneElementInit()

            builder.useElement("elem\$width << 2")
            val builtSignal = builder.build()

            assertSignalDimensionEquals(signalStub, builtSignal)
        }

        @Test
        fun ` 'less than or equals ' works`() {
            val builder = builderOneElementInit()

            builder.useElement("elem\$width <= 1")
            val builtSignal = builder.build()

            assertSignalDimensionEquals(signalStub, builtSignal)
        }

        @Test
        fun ` 'wrong or no-op ' works`() {
            val builder = builderOneElementInit()

            builder.useElement("elem\$width")
            val builtSignal = builder.build()

            assertSignalDimensionEquals(signalStub, builtSignal)
        }

        @Test
        fun ` 'bind' works`() {
            val builder = builderOneElementInit()

            val elems = mapOf<String, (String, String) -> Boolean>(
                "elem\$x & test" to { x, y -> x == y },
                "elem\$width & test" to { x, y -> x == y }
            )

            builder.useElements(elems)
            val builtSignal = builder.build()

            assertSignalDimensionEquals(alwaysFalseSignal, builtSignal, 1)
        }
    }

    private fun assertSignalDimensionEquals(
        expected: SpatialTemporalSignal<List<Boolean>>,
        actual: SpatialTemporalSignal<List<Boolean>>,
        dimension: Int = 0
    ) {
        val expectedArray = dimToArray(expected, 0)
        val actualArray = dimToArray(actual, dimension)
        assertArrayEquals(expectedArray, actualArray)
    }

    private val signalStub = run {
        val signal = SpatialTemporalSignal<List<Boolean>>(9)
        signal.add(0.0) {
            if (it in listOf(0, 1, 3, 4, 6, 7)) {   //  1 1 0
                listOf(true)                   //  1 1 0
            } else {                                //  0 0 0
                listOf(false)
            }
        }
        signal.add(1.0) {
            if (it in listOf(0, 1, 3, 4, 6, 7)) {   //  1 1 0
                listOf(true)                   //  1 1 0
            } else {                                //  0 0 0
                listOf(false)
            }
        }
        signal
    }

    private fun dimToArray(
        signal: SpatialTemporalSignal<List<Boolean>>,
        dimension: Int
    ) =
        signal.toArray { booleanToDouble(it[dimension]) }


    private fun booleanToDouble(value: Boolean): Double {
        return when (value) {
            true -> 1.0
            else -> 0.0
        }
    }

    private fun builderMalformedData(): TraceBuilder {
        val grid = Grid(3, 3)
        val data = listOf(
            mapOf(
                "vvp_width" to "2",
                "elem::0::y" to "2"
            )
        )
        val builder = TraceBuilder(grid, data)
        builder.useMetadata()
        return builder
    }

    private fun builderMetadataInit(): TraceBuilder {
        val grid = Grid(3, 3)
        val data = listOf(
            mapOf(
                "vvp_width" to "2",
                "vvp_height" to "2"
            ),
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
                "wm-test" to "0",
                "elem::size::" to "1",
                "elem::0::x" to "0",
                "elem::0::y" to "0",
                "elem::0::width" to "1",
                "elem::0::height" to "2"
            ),
            mapOf(
                "wm-test" to "0",
                "elem::size::" to "1",
                "elem::0::x" to "0",
                "elem::0::y" to "0",
                "elem::0::width" to "1",
                "elem::0::height" to "2"
            )
        )
        return TraceBuilder(grid, data)
    }

    private fun builderOneElementFlashingInit(): TraceBuilder {
        val grid = Grid(3, 3)
        val data = listOf(
            mapOf(
                "wm-test" to "0",
                "elem::size::" to "1",
                "elem::0::x" to "0",
                "elem::0::y" to "0",
                "elem::0::width" to "3",
                "elem::0::height" to "3"
            ),
            mapOf(
                "wm-test" to "0",
            )
        )
        return TraceBuilder(grid, data)
    }
}
