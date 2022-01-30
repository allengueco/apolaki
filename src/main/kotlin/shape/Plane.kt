package shape

import Utils.Companion.EPSILON
import scene.Intersection
import scene.Intersections
import core.Point
import core.Ray
import core.Tuple.Companion.vector
import kotlin.math.abs

class Plane : Shape() {
    override fun localIntersect(localRay: Ray): Intersections? {
        return when {
            abs(localRay.dir.y) < EPSILON -> null
            else -> {
                // This is only true if the plane is xz
                val t = -localRay.origin.y / localRay.dir.y
                listOf(Intersection(t, this))
            }
        }
    }

    override fun localNormal(localPoint: Point) = vector(0, 1, 0)

}
