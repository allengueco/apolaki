package patterns

import base.Pattern
import core.Color
import core.Point
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class Ring(val a: Color, val b: Color) : Pattern() {
    override fun color(point: Point, c1: Color, c2: Color): Color {
        val f = floor(sqrt(point.x.pow(2) + point.z.pow(2))).toInt()

        return when (f % 2) {
            0 -> c1
            else -> c2
        }

    }

    override fun get(point: Point): Color {
        return color(point, a, b)
    }

}
