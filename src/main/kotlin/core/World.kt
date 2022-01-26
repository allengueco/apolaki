package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point

class World(
    val light: Light? = Light(point(-10, 10, -10), color(1, 1, 1)),
    val objects: MutableList<WorldObject> = mutableListOf(
        Sphere().apply {
            material.color = color(0.8, 1.0, 0.6)
            material.diffuse = 0.7
            material.specular = 0.2
        },
        Sphere().apply {
            transform = transform.scale(0.5, 0.5, 0.5)
        }
    ),
) : Intersect {
    val empty: Boolean by lazy { objects.isEmpty() }

    override fun intersect(ray: Ray): Intersections? {
        val intersections = objects.mapNotNull { it.intersect(ray) }
            .flatten()
            .sortedBy { it.t.toDouble() }


        return intersections.ifEmpty { null }
    }

    companion object {
        fun empty() = World(null, mutableListOf())
    }

    fun shade(comps: Computation): Color {
        return if (light != null) {
            val isShadowed = isShadowed(comps.overPoint)
            comps.intersection
                .obj
                .material
                .lighting(light, comps.point, comps.eyeVector, comps.normalVector, isShadowed)
        } else {
            color(0.5, 0.5, 0.5) // shade black if empty? idk if right
        }
    }

    operator fun contains(obj: WorldObject) = obj in objects
    fun color(r: Ray) = intersect(r)
        ?.hit()
        ?.compute(r)
        ?.let { shade(it) }
        ?: color(0, 0, 0)

    fun render(camera: Camera): Canvas {
        val image = Canvas(camera.hSize, camera.vSize)

        (0 until camera.vSize). forEach { y ->
            (0 until camera.hSize). forEach { x ->
                val r = camera.cast(x, y)
                val color = color(r)
                image[x, y] = color
            }
        }

        return image
    }

    override fun toString(): String {
        return "World(light=$light, objects=$objects)"
    }

    fun isShadowed(point: Tuple): Boolean {
        // If the world doesn't have a light, then every point is shadowed
        return if (light == null) {
            true
        } else {
            val v = light.position - point
            val distance = v.magnitude()
            val dir = v.normalize()

            val r = Ray(point, dir)
            val hit = intersect(r)?.hit()

            (hit != null && hit.t.toDouble() < distance)
        }
    }
}
