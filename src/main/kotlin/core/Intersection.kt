package core

class Intersection(val t: Number, val obj: WorldObject) {
    fun compute(r: Ray) = Computation(this, r)
}

data class Computation(
    val intersection: Intersection,
    val ray: Ray
) {
    val point: Point = ray.at(intersection.t)
    val eyeVector: Vector = -ray.dir
    val normalVector: Vector
    val inside: Boolean
    init {
        val normal = intersection.obj.normal(point)
        val isOpposite = normal.dot(eyeVector) < 0.0
        if (isOpposite) {
            inside = true
            normalVector = -normal
        }
        else {
            inside = false
            normalVector = normal
        }
    }
}

typealias Intersections = List<Intersection>

fun Intersections.count() = this.size

fun Intersections.hit() = this.filter { it.t.toDouble() > 0 }.minByOrNull { it.t.toDouble() }

