package core

import core.Tuple.Companion.point
import kotlin.math.pow
import kotlin.math.sqrt

class Sphere(val radius: Number) {
    fun intersect(ray: Ray): Pair<Number?, Number?>? {
        val sphereToRay = ray.origin - point(0, 0, 0)

        val a = ray.dir.dot(ray.dir)
        val b = 2 * ray.dir.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1

        val discriminant = b.pow(2) - 4 * a * c

        if (discriminant < 0) return null

        val t = { sqrtD: Double -> (-b + sqrtD) / (2.0 * a)}

        return t(-sqrt(discriminant)) to t(sqrt(discriminant))
    }
}
