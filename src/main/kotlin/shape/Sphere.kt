package shape

import core.*
import core.Tuple.Companion.point
import scene.Intersection
import scene.Intersections
import kotlin.math.pow
import kotlin.math.sqrt

class Sphere : Shape() {

    override fun localIntersect(localRay: Ray): Intersections? {
        val sphereToRay = localRay.origin - point(0, 0, 0)

        val a = localRay.dir.dot(localRay.dir)
        val b = 2 * localRay.dir.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1

        val discriminant = b.pow(2) - 4 * a * c

        if (discriminant < 0) return null

        val t = { sqrtD: Double -> (-b + sqrtD) / (2.0 * a) }

        return listOf(
            Intersection(t(-sqrt(discriminant)), this),
            Intersection(t(sqrt(discriminant)), this)
        )
    }

    override fun localNormal(localPoint: Point): Tuple {
        return (localPoint - point(0, 0, 0)).normalize()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sphere

        if (material != other.material) return false
        if (transform != other.transform) return false

        return true
    }

    override fun hashCode(): Int {
        var result = material.hashCode()
        result = 31 * result + transform.hashCode()
        return result
    }
}
