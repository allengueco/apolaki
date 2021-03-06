package patterns

import base.Pattern
import core.Color
import core.Point
import kotlin.math.floor

class Gradient(val a: Color, val b: Color): Pattern() {
    /**
     * Lerp function to blend colors
     */
    override fun color(point: Point, c1: Color, c2: Color): Color {
        val distance = b - a
        val fraction = point.x - floor(point.x)
        return a + distance * fraction
    }

    override fun get(point: Point): Color {
        return color(point, a, b)
    }
}
