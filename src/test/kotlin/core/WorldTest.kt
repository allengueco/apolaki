package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import scene.World
import shape.Shape
import scene.Intersection
import scene.Light
import scene.count
import shape.Plane
import shape.Sphere
import kotlin.math.sqrt

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

    @Test
    fun `There is no shadow when nothing is collinear with point and light`() {
        val w = World()
        val p = point(0, 10, 0)

        assertFalse(w.isShadowed(p))
    }

    @Test
    fun `The shadow when an object is between the point and the light`() {
        val w = World()
        val p = point(10, -10, 10)

        assertTrue(w.isShadowed(p))
    }

    @Test
    fun `There is no shadow when an object is behind the light`() {
        val w = World()
        val p = point(-20, 20, -20)

        assertFalse(w.isShadowed(p))
    }

    @Test
    fun `There is no shadow when an object is behind the point`() {
        val w = World()
        val p = point(-2, 2, -2)

        assertFalse(w.isShadowed(p))
    }

    @Test
    fun `shade() is given an intersection in shadow`() {
        val s1 = Sphere()
        val s2 = Sphere().apply {
            transform = transform.translate(0, 0, 10)
        }
        val objects = mutableListOf<Shape>(s1, s2)
        val w = World(
            light = Light(point(0, 0, -10), color(1, 1, 1)),
            objects = objects
        )

        val r = Ray(point(0, 0, 5), vector(0, 0, 1))
        val i = Intersection(4, s2)

        val comps = i.compute(r)
        val c = w.shade(comps)

        assertEquals(color(0.1, 0.1, 0.1), c)
    }

    @Test
    fun `The reflected color for a nonreflective material`() {
        val w = World()
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val shape = w.objects[1].apply {
            material.ambient = 1.0
        }
        val i = Intersection(1.0, shape)

        val comps = i.compute(r)
        val color = w.reflectedColor(comps)

        assertEquals(color(0, 0, 0), color)
    }

    @Test
    fun `The reflected color for a reflective material`() {
        val w = World()
        val shape = Plane().apply {
            material.reflective = 0.5
            transform = transform.translate(0, -1, 0)
        }
        w.objects.add(shape)
        val r = Ray(point(0, 0, -3), vector(0, -sqrt(2.0)/2, sqrt(2.0)/2))
        val i = Intersection(sqrt(2.0), shape)

        val comps = i.compute(r)
        val color = w.reflectedColor(comps)

        assertEquals(color(0.19032, 0.2379, 0.14274), color)
    }
}