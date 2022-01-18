package core

import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class SphereTest {

    /**
     * Helper method that must return a sphere that is unique each time
     */
    private fun sphere() = Sphere(Random.nextDouble(10.0))

    @Test
    fun `A ray intersects a sphere at two points`() {
        val ray = Ray(point(0, 0, -5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.intersect(ray)

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

        val xs = sphere.intersect(ray)

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

        val xs = sphere.intersect(ray)

        assertNull(xs)
    }

    @Test
    fun `A ray originates inside a sphere`() {
        val ray = Ray(point(0, 0, 0), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.intersect(ray)

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

        val xs = sphere.intersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(2, xs!!.count()) },
            { assertEquals(sphere, xs!![0].obj) },
            { assertEquals(sphere, xs!![1].obj) }
        )
    }

    @Test
    fun `A sphere's default transformation`() {
        val s = sphere()

        assertEquals(Matrix.identity(), s.transform)
    }

    @Test
    fun `Changing a sphere's transformation`() {
        val s = sphere()
        val t = Matrix.translation(2, 3, 4)

        s.transform = t

        assertEquals(t, s.transform)
    }

    @Test
    fun `Intersecting a scaled sphere with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere()

        s.transform = Matrix.scaling(2, 2, 2)
        val xs = s.intersect(r)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(2, xs!!.count()) },
            { assertEquals(3.0, xs!![0].t) },
            { assertEquals(7.0, xs!![1].t) }
        )
    }

    @Test
    fun `Intersecting a translated sphere with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere()

        s.transform = Matrix.translation(5, 0, 0)
        val xs = s.intersect(r)

        assertNull(xs)
    }
}