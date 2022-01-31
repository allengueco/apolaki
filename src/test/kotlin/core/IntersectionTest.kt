package core

import Utils.Companion.EPSILON
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import scene.Intersection
import scene.count
import scene.hit
import shape.Plane
import shape.Sphere
import kotlin.math.sqrt

internal class IntersectionTest {
    /**
     * Helper method that must return a sphere that is unique each time
     */
    private fun sphere() = Sphere()
    @Test
    fun `An intersection encapsulates t and object`() {
        val s = sphere()
        val t = 3.5
        val i = Intersection(t, s)

        assertAll(
            { assertEquals(3.5, i.t) },
            { assertEquals(s, i.obj) }
        )
    }

    @Test
    fun `Aggregating intersections`() {
        val s = sphere()
        val i1 = Intersection(1, s)
        val i2 = Intersection(2, s)
        val xs = listOf(i1, i2)

        assertAll(
            { assertEquals(2, xs.count()) },
            { assertEquals(1, xs[0].t) },
            { assertEquals(2, xs[1].t) }
        )
    }

    @Test
    fun `The hit, when all intersections have positive t`() {
        val s = sphere()
        val i1 = Intersection(1, s)
        val i2 = Intersection(2, s)
        val xs = listOf(i1, i2)

        val i = xs.hit()

        assertEquals(i1, i)
    }

    @Test
    fun `The hit, when some intersections have negative t`() {
        val s = sphere()
        val i1 = Intersection(-1, s)
        val i2 = Intersection(1, s)
        val xs = listOf(i1, i2)

        val i = xs.hit()

        assertEquals(i2, i)
    }

    @Test
    fun `The hit, when all intersections have negative t`() {
        val s = sphere()
        val i1 = Intersection(-2, s)
        val i2 = Intersection(-1, s)
        val xs = listOf(i1, i2)

        val i = xs.hit()

        assertNull(i)
    }

    @Test
    fun `The hit is always the lowest non-negative intersection`() {
        val s = sphere()
        val i1 = Intersection(5, s)
        val i2 = Intersection(7, s)
        val i3 = Intersection(-3, s)
        val i4 = Intersection(2, s)

        val xs = listOf(i1, i2, i3, i4)

        val i = xs.hit()

        assertEquals(i4, i)
    }

    @Test
    fun `Precomputing the state of an intersection`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(4, shape)

        val comps = i.compute(r)

        assertAll(
            { assertEquals(comps.intersection.t, i.t) },
            { assertEquals(comps.intersection.obj, i.obj) },
            { assertEquals(comps.point, point(0, 0, -1)) },
            { assertEquals(comps.eyeVector, vector(0, 0, -1)) },
            { assertEquals(comps.normalVector, vector(0, 0, -1)) }
        )
    }

    @Test
    fun `The hit, when an intersection occurs on the outside`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(4, shape)

        val comps = i.compute(r)

        assertFalse(comps.inside)
    }

    @Test
    fun `The hit, when an intersection occurs on the inside`() {
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(1, shape)

        val comps = i.compute(r)
        assertAll(
            { assertTrue(comps.inside) },
            { assertEquals(comps.point, point(0, 0, 1)) },
            { assertEquals(comps.eyeVector, vector(0, 0, -1)) },
            { assertEquals(comps.normalVector, vector(0, 0, -1)) }
        )
    }

    @Test
    fun `The hit should offset the point`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = Sphere().apply {
            transform = transform.translate(0, 0, 1)
        }
        val i = Intersection(5, shape)

        val comps = i.compute(r)

        assertAll(
            { assertTrue(comps.overPoint.z < -EPSILON/2) },
            { assertTrue(comps.point.z > comps.overPoint.z) }
        )
    }

    @Test
    fun `Precomputing the reflection vector`() {
        val shape = Plane()
        val r = Ray(point(0, 1, -1), vector(0, -sqrt(2.0)/2, sqrt(2.0)/2))
        val i = Intersection(sqrt(2.0), shape)

        val comps = i.compute(r)

        assertEquals(vector(0, sqrt(2.0)/2, sqrt(2.0)/2), comps.reflectVector)
    }

    companion object IntersectionsParameters {
        @JvmStatic
        fun intersectionArgs(): List<Arguments> {
            Stream.of(
                Arguments
            )
        }
    }

    @Test
    fun `Finding n1 and n2 at various intersections`() {
        val a = Sphere.glass().apply {
            transform = transform.scale(2, 2, 2)
            material.refractiveIndex = 1.5
        }
        val b = Sphere.glass().apply {
            transform = transform.translate(0, 0, -0.25)
            material.refractiveIndex = 2.0
        }
        val c = Sphere.glass().apply {
            transform = transform.translate(0, 0, 0.25)
            material.refractiveIndex = 2.5
        }
        val r = Ray(point(0, 0, -4), vector(0, 0, 1))
        val xs =
    }

}


