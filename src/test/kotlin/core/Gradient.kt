package core

import kotlin.math.floor

class Gradient(val a: Color, val b: Color): Pattern() {
    /**
     * Lerp function to blend colors
     */
    override fun color(point: Point, c1: Color, c2: Color): Color = a + (b - a) * (point.x - floor(point.x))

    override fun get(point: Point): Color {
        return color(point, a, b)
    }
}
