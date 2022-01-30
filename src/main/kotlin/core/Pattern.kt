package core

sealed interface Pattern {
    fun color(point: Point, c1: Color, c2: Color): Color
    operator fun get(point: Point): Color
}