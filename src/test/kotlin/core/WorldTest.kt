package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class WorldTest {
    @Test
    fun `Creating a world`() {
        val w = World.empty()

        assertTrue(w.empty)
        assertNull(w.light)
    }

    @Test
    fun `The default world`() {
        val light = Light(point(-10, 10, -10), color(1, 1, 1))
        val s1 = Sphere().apply {
            material.color = color(0.8, 1.0, 0.6)
            material.diffuse = 0.7
            material.specular = 0.2
        }

        val s2 = Sphere().apply {
            transform = transform.scale(0.5, 0.5, 0.5)
        }

        val w = World()

        assertAll(
            { assertEquals(light, w.light) },
            { assertTrue(s1 in w) },
            { assertTrue(s2 in w) }
        )
    }

    @Test
    fun `Intersect a world with a ray`() {
        val w = World()
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))

        val xs = w.intersect(r)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(4, xs!!.count()) },
            { assertEquals(4.0, xs!![0].t) },
            { assertEquals(4.5, xs!![1].t) },
            { assertEquals(5.5, xs!![2].t) },
            { assertEquals(6.0, xs!![3].t) },
        )
    }

    @Test
    fun `Shading an intersection`() {
        val w = World()
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = w.objects[0]
        val i = Intersection(4, shape)

        val comps = i.compute(r)
        val c = w.shade(comps)

        assertEquals(color(0.38066, 0.47583, 0.2855), c)
    }

    @Test
    fun `Shading an intersection from the inside`() {
        val w = World(
            light = Light(point(0, 0.25, 0), color(1, 1, 1))
        )
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val shape = w.objects[1]
        val i = Intersection(0.5, shape)

        val comps = i.compute(r)
        val c = w.shade(comps)

        assertEquals(color(0.90498, 0.90498, 0.90498), c)
    }

    @Test
    fun `The color when a ray misses`() {
        val w = World()
        val r = Ray(point(0, 0, -5), vector(0, 1, 0))

        val c = w.color(r)

        assertEquals(color(0, 0, 0), c)
    }

    @Test
    fun `The color when a ray hits`() {
        val w = World()
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))

        val c = w.color(r)

        assertEquals(color(0.380666, 0.47583, 0.2855), c)
    }

    @Test
    fun `The color with an intersection behind the ray`() {
        val w = World().apply {
            objects[0].material.ambient = 1.0
            objects[1].material.ambient = 1.0
        }
        val r = Ray(point(0, 0, 0.75), vector(0, 0, -1))

        val c = w.color(r)

        assertEquals(w.objects[1].material.color, c)
    }
}