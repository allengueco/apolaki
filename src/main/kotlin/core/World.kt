package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point

class World(
    val light: Light? = null,
    private val objects: MutableCollection<WorldObject> = mutableListOf(),
) : Intersect {
    val empty: Boolean by lazy { objects.isEmpty() }

    override fun intersect(ray: Ray): Intersections? {
        val intersections = objects.mapNotNull { it.intersect(ray) }
            .flatten()
            .sortedBy { it.t.toDouble() }


        return intersections.ifEmpty { null }
    }

    companion object {
        fun default() = World(
            light = Light(point(-10, 10, -10), color(1, 1, 1)),
            objects = mutableListOf(
                Sphere().apply {
                    material.color = color(0.8, 1.0, 0.6)
                    material.diffuse = 0.7
                    material.specular = 0.2
                },
                Sphere().apply {
                    transform = transform.scale(0.5, 0.5, 0.5)
                }
            )
        )
    }

    operator fun contains(obj: WorldObject) = obj in objects
}
