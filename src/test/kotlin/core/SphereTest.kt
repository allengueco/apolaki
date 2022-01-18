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
            { assertEquals(4.0, xs?.first) },
            { assertEquals(6.0, xs?.second) }
        )
    }
    @Test
    fun `A ray intersects a sphere at a tangent`() {
        val ray = Ray(point(0, 1, -5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.intersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(5.0, xs?.first) },
            { assertEquals(5.0, xs?.second) }
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
            { assertEquals(-1.0, xs?.first) },
            { assertEquals(1.0, xs?.second) }
        )
    }

    @Test
    fun `A sphere is behind a ray`() {
        val ray = Ray(point(0, 0, 5), vector(0, 0, 1))
        val sphere = sphere()

        val xs = sphere.intersect(ray)

        assertAll(
            { assertNotNull(xs) },
            { assertEquals(-6.0, xs?.first) },
            { assertEquals(-4.0, xs?.second) }
        )
    }
}