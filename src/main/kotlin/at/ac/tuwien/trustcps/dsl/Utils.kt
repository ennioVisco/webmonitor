package at.ac.tuwien.trustcps.dsl

import kotlin.math.*

data class Color(val red: Int, val green: Int, val blue: Int) {

    fun contrastRatio(other: Color): Double {
        val luminance1 = this.relativeLuminance()
        val luminance2 = other.relativeLuminance()
        
        return if (luminance1 > luminance2)
            (luminance1 + 0.05) / (luminance2 + 0.05)
        else
            (luminance2 + 0.05) / (luminance1 + 0.05)
    }

    private fun relativeLuminance(): Double {
        val red = this.red / 255.0
        val green = this.green / 255.0
        val blue = this.blue / 255.0
        return 0.2126 * linearize(red) +
                0.7152 * linearize(green) +
                0.0722 * linearize(blue)
    }

    private fun linearize(color: Double): Double {
        return if (color <= 0.03928) {
            color / 12.92
        } else {
            ((color + 0.055) / 1.055).pow(2.4)
        }
    }
}

fun String.asColor(): Color {
    val format = this.substring(0, 4)
    if (format != "rgba")
        throw IllegalArgumentException("Not a valid rgb color: $format")

    val rgb = this.substring(5, this.length - 1).split(", ")
    return Color(rgb[0].toInt(), rgb[1].toInt(), rgb[2].toInt())
}

fun parsePixels(value: String): Double {
    return value.substring(0, value.length - 2).toDouble()
}
