package patterns

import base.Pattern
import core.Color
import core.Point
import kotlin.math.floor

class Stripe(val a: Color, val b: Color) : Pattern() {
    override fun color(point: Point, c1: Color, c2: Color): Color {
        return when (floor(point.x).toInt() % 2) {
            0 -> c1
            else -> c2
        }
    }

    override fun get(point: Point): Color = color(point, a, b)
}
