package core

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
        val r = Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(4, shape)

        val comps = i.compute(r)

        assertAll(
            { assertEquals(comps.intersection.t, i.t) },
            { assertEquals(comps.intersection.obj, i.obj) },
            { assertEquals(comps.point, Tuple.point(0, 0, -1)) },
            { assertEquals(comps.eyeVector, Tuple.vector(0, 0, -1)) },
            { assertEquals(comps.normalVector, Tuple.vector(0, 0, -1)) }
        )
    }

    @Test
    fun `The hit, when an intersection occurs on the outside`() {
        val r = Ray(Tuple.point(0, 0, -5), Tuple.vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(4, shape)

        val comps = i.compute(r)

        assertFalse(comps.inside)
    }

    @Test
    fun `The hit, when an intersection occurs on the inside`() {
        val r = Ray(Tuple.point(0, 0, 0), Tuple.vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(1, shape)

        val comps = i.compute(r)
        assertAll(
            { assertTrue(comps.inside) },
            { assertEquals(comps.point, Tuple.point(0, 0, 1)) },
            { assertEquals(comps.eyeVector, Tuple.vector(0, 0, -1)) },
            { assertEquals(comps.normalVector, Tuple.vector(0, 0, -1)) }
        )

    }
}