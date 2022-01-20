package core

class Intersection(val t: Number, val obj: WorldObject)

typealias Intersections = List<Intersection>

fun Intersections.count() = this.size

fun Intersections.hit() = this.filter { it.t.toDouble() > 0 }.minByOrNull { it.t.toDouble() }!!

