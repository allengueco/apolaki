package core

import core.Tuple.Companion.color

class Canvas(val width: Int, val height: Int) {
    private val pixels = MutableList(height) { MutableList(width) { color(0, 0, 0) } }
    operator fun get(x: Int, y: Int) = pixels[y][x]
    operator fun set(x: Int, y: Int, color: Tuple) {
        pixels[y][x] = color
    }

    private fun ppmHeader() = "P3\n$width $height\n255\n"

    private fun ppmColor(color: Tuple) = listOf(color.red, color.green, color.blue)
        .map { clamp(it, .0, .999) }
        .map { it * 256 }
        .map { it.toInt() }
        .joinToString(" ")

    private fun ppmPixelData(): String {
        return pixels.joinToString("\n") { row -> row.joinToString(" ") { c -> ppmColor(c) } }
    }

    private fun clamp(value: Int, range: IntRange = 0..255): Int {
        return when (value) {
            in range -> value
            else -> if (value < range.first) range.first else range.last
        }
    }

    private fun clamp(value: Double, min: Double, max: Double): Double {
        return when (value) {
            in min..max -> value
            else -> if (value < min) min else max
        }
    }

    fun ppm() = ppmHeader() + ppmPixelData() + "\n"

    fun pixels() = pixels.flatten()
}