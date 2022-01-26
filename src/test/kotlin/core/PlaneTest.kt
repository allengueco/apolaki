package core

import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlaneTest {
    @Test
    fun `The normal of a plane is constant everywhere`() {
        val p = Plane()

        val n1 = p.localNormal(point(0, 0, 0))
        val n2 = p.localNormal(point(10, 0, -10))
        val n3 = p.localNormal(point(-5, 0, 150))

        assertAll(
            { assertEquals(vector(0, 1, 0), n1) },
            { assertEquals(vector(0, 1, 0), n2) },
            { assertEquals(vector(0, 1, 0), n3) },
        )
    }

    @Test
    fun `Intersect with a parallel to the plane`() {
        val p = Plane()
        val r = Ray(point(0, 10, 0), vector(0, 0, 1))

        val xs = p.localIntersect(r)

        assertNull(xs)
    }

    @Test
    fun `Intersect with a coplanar ray`() {
        val p = Plane()
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))

        val xs = p.localIntersect(r)

        assertNull(xs)
    }

    @Test
    fun `A ray intersecting a plane from above`() {
        val p = Plane()
        val r = Ray(point(0, 1, 0), vector(0, -1, 0))

        val xs = p.localIntersect(r)

        assertAll(
            { assertEquals(1, xs!!.count()) },
            { assertEquals(1.0, xs!![0].t) },
            { assertEquals(p, xs!![0].obj) }
        )
    }

    @Test
    fun `A ray intersecting a plane from below`() {
        val p = Plane()
        val r = Ray(point(0, -1, 0), vector(0, 1, 0))

        val xs = p.localIntersect(r)

        assertAll(
            { assertEquals(1, xs!!.count()) },
            { assertEquals(1.0, xs!![0].t) },
            { assertEquals(p, xs!![0].obj) }
        )
    }
}