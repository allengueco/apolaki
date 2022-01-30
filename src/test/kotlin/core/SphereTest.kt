package core

import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shape.Shape
import scene.count
import shape.Sphere
import kotlin.math.sqrt
import kotlin.test.assertIs

internal class SphereTest {

    /**
     * Helper method that must return a sphere that is unique each time
     */
    private fun sphere() = Sphere()

    @Test
    fun `A ray intersects a sphere at two points`() {
        val ray = Ray(point(0, 0, -5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.localIntersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(2, xs!!.count()) },
            { assertEquals(sphere, xs!![0].obj) },
            { assertEquals(sphere, xs!![1].obj) }
        )
    }
    @Test
    fun `A ray intersects a sphere at a tangent`() {
        val ray = Ray(point(0, 1, -5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.localIntersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(2, xs!!.count()) },
            { assertEquals(sphere, xs!![0].obj) },
            { assertEquals(sphere, xs!![1].obj) }
        )
    }

    @Test
    fun `A ray misses a sphere`() {
        val ray = Ray(point(0, 2, -5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.localIntersect(ray)

        assertNull(xs)
    }

    @Test
    fun `A ray originates inside a sphere`() {
        val ray = Ray(point(0, 0, 0), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.localIntersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(2, xs!!.count()) },
            { assertEquals(sphere, xs!![0].obj) },
            { assertEquals(sphere, xs!![1].obj) }
        )
    }

    @Test
    fun `A sphere is behind a ray`() {
        val ray = Ray(point(0, 0, 5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.localIntersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(2, xs!!.count()) },
            { assertEquals(sphere, xs!![0].obj) },
            { assertEquals(sphere, xs!![1].obj) }
        )
    }

    @Test
    fun `The localNormal on a sphere at a point on the x axis`() {
        val s = sphere()

        val n = s.localNormal(point(1, 0, 0))

        assertEquals(vector(1, 0, 0), n)
    }

    @Test
    fun `The localNormal on a sphere at a point on the y axis`() {
        val s = sphere()

        val n = s.localNormal(point(0, 1, 0))

        assertEquals(vector(0, 1, 0), n)
    }

    @Test
    fun `The localNormal on a sphere at a point on the z axis`() {
        val s = sphere()

        val n = s.localNormal(point(0, 0, 1))

        assertEquals(vector(0, 0, 1), n)
    }

    @Test
    fun `The localNormal on a sphere at a nonaxial point`() {
        val s = sphere()

        val n = s.localNormal(point(sqrt(3.0)/3, sqrt(3.0)/3, sqrt(3.0)/3))

        assertEquals(vector(sqrt(3.0)/3, sqrt(3.0)/3, sqrt(3.0)/3), n)
    }

    @Test
    fun `The localNormal is a normalized vector`() {
        val s = sphere()

        val n = s.localNormal(point(sqrt(3.0)/3, sqrt(3.0)/3, sqrt(3.0)/3))

        assertEquals(n.normalize(), n)
    }

    @Test
    fun `A Sphere is a Shape`() {
        assertIs<Shape>(sphere(), "A Sphere is a Shape")
    }
}