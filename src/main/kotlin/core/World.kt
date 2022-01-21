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
            comps.intersection
                .obj
                .material
                .lighting(light, comps.point, comps.eyeVector, comps.normalVector)
        } else {
            color(0, 0, 0) // shade black if empty? idk if right
        }
    }

    operator fun contains(obj: WorldObject) = obj in objects
    fun color(r: Ray) = intersect(r)
        ?.hit()
        ?.compute(r)
        ?.let { shade(it) }
        ?: color(0, 0, 0)
}
