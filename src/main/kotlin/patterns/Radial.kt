package patterns

import base.Pattern
import core.Color
import core.Point
import core.Tuple.Companion.point
import kotlin.math.floor

class Radial(val a: Color, val b: Color) : Pattern() {
    override fun color(point: Point, c1: Color, c2: Color): Color {
        val distance = point - point(0, 0, 0)
        val fraction = let {
            val m = point.apply { w = 0.0 }.magnitude()
            m - floor(m)
        }
        return a + distance * fraction
    }

    override fun get(point: Point): Color {
        return color(point, a, b)
    }
}