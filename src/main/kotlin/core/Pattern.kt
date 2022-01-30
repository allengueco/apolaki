package core

abstract class Pattern: Transform() {
    abstract fun color(point: Point, c1: Color, c2: Color): Color

    /**
     * [stripe_at()] in the book
     */
    abstract operator fun get(point: Point): Color

    /**
     * Returns the color for the given pattern, on the given object,
     * on the given world space point. Should respect the transformation
     * on both the patter and the object while doing so.
     *
     * [pattern_at_shape()] in book
     */
    operator fun get(obj: Shape, worldPoint: Point): Color {
        val objPoint = obj.transform.inverse() * worldPoint
        val patternPoint = this.transform.inverse() * objPoint

        return this[patternPoint]
    }
}