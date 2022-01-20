package core

import core.Tuple.Companion.point
import kotlin.math.pow
import kotlin.math.sqrt

class Sphere : Object {
    override var material = Material()
    var transform = Matrix.identity()
    override fun intersect(ray: Ray): Intersections? {
        val ray = ray.transform(transform.inverse())
        val sphereToRay = ray.origin - point(0, 0, 0)

        val a = ray.dir.dot(ray.dir)
        val b = 2 * ray.dir.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1

        val discriminant = b.pow(2) - 4 * a * c

        if (discriminant < 0) return null

        val t = { sqrtD: Double -> (-b + sqrtD) / (2.0 * a) }

        return listOf(
            Intersection(t(-sqrt(discriminant)), this),
            Intersection(t(sqrt(discriminant)), this)
        )
    }

    override fun normal(worldPoint: Tuple): Tuple {
        val objectPoint = transform.inverse() * worldPoint
        val objectNormal = objectPoint - point(0, 0, 0)
        val worldNormal = transform.inverse().transpose() * objectNormal
        worldNormal.w = 0.0 // hacky way to preserve the normal being a vector
        return worldNormal.normalize()
    }
}
