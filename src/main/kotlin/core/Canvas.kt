package core

import core.Tuple.Companion.color
import java.io.File

class Canvas(val width: Int, val height: Int) {
    var pixels = MutableList(height) { MutableList(width) { color(0, 0, 0) } }
    operator fun get(x: Int, y: Int) = pixels[y][x]
    operator fun set(x: Int, y: Int, color: Tuple) {
        if (y !in 0..height || x !in 0..width) return
        pixels[y][x] = color
    }

    constructor(width: Int, height: Int, init: Tuple) : this(width, height) {
        pixels = pixels.map { row -> row.map { init }.toMutableList() }.toMutableList()
    }

    private fun ppmHeader() = "P3\n$width $height\n255\n"

    private fun ppmColor(color: Tuple) = listOf(color.red, color.green, color.blue)
        .map { clamp(it, .0, .999) }
        .map { it * 256 }
        .map { it.toInt() }
        .joinToString(" ")

    private fun ppmPixelData(): String {
        return pixels().joinToString("\n") { c -> ppmColor(c)}
    }

    private fun clamp(value: Double, min: Double, max: Double): Double {
        return when (value) {
            in min..max -> value
            else -> if (value < min) min else max
        }
    }

    fun ppm() = ppmHeader() + ppmPixelData() + "\n"

    fun pixels() = pixels.flatten()

    fun saveCanvasToFile(fileName: String) {
        File(fileName).createNewFile()
        File(fileName).writeText(ppm())
    }
}