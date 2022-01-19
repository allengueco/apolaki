package core

import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

internal class SphereTest {

    /**
     * Helper method that must return a sphere that is unique each time
     */
    private fun sphere() = Sphere()

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

    @Test
    fun `The normal on a sphere at a point on the x axis`() {
        val s = sphere()

        val n = s.normal(point(1, 0, 0))

        assertEquals(vector(1, 0, 0), n)
    }

    @Test
    fun `The normal on a sphere at a point on the y axis`() {
        val s = sphere()

        val n = s.normal(point(0, 1, 0))

        assertEquals(vector(0, 1, 0), n)
    }

    @Test
    fun `The normal on a sphere at a point on the z axis`() {
        val s = sphere()

        val n = s.normal(point(0, 0, 1))

        assertEquals(vector(0, 0, 1), n)
    }

    @Test
    fun `The normal on a sphere at a nonaxial point`() {
        val s = sphere()

        val n = s.normal(point(sqrt(3.0)/3, sqrt(3.0)/3, sqrt(3.0)/3))

        assertEquals(vector(sqrt(3.0)/3, sqrt(3.0)/3, sqrt(3.0)/3), n)
    }

    @Test
    fun `The normal is a normalized vector`() {
        val s = sphere()

        val n = s.normal(point(sqrt(3.0)/3, sqrt(3.0)/3, sqrt(3.0)/3))

        assertEquals(n.normalize(), n)
    }

    @Test
    fun `Computing the normal on a translated sphere`() {
        val s = sphere()
        s.transform = s.transform.translate(0, 1, 0)

        val n = s.normal(point(0.0, 1.70711, -0.70711))

        assertEquals(vector(0.0, 0.70711, -0.70711), n)
    }

    @Test
    fun `Computing the normal on a transformed sphere`() {
        val s = sphere()
        s.transform = s.transform
            .rotateZ(PI/5)
            .scale(1, 0.5, 1)

        val n = s.normal(point(0.0, sqrt(2.0)/2, -sqrt(2.0)/2))

        assertEquals(vector(0.0, 0.97014, -0.24254), n)
    }

    @Test
    fun `A sphere has a default material`() {
        val s = sphere()

        val m = s.material

        assertEquals(Material(), m)
    }

    @Test
    fun `A sphere may be assigned material`() {
        val s = sphere()
        val m = s.material
        m.ambient = 1.0

        s.material = m

        assertEquals(m, s.material)
    }
}