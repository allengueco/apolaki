package patterns

import base.Pattern
import core.Color
import core.Point
import kotlin.math.floor

class Checkers(val a: Color, val b: Color) : Pattern() {
    override fun color(point: Point, c1: Color, c2: Color): Color {
        val sum = listOf(point.x, point.y, point.z).sumOf { floor(it).toInt() }

        return when (sum % 2) {
            0 -> c1
            else -> c2
        }
    }

    override fun get(point: Point): Color {
        return color(point, a, b)
    }

}
