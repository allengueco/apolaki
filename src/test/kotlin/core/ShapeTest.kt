package core

import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shape.Shape
import scene.Intersections
import scene.Material
import kotlin.math.PI
import kotlin.math.sqrt

internal class ShapeTest {
    class TestShape: Shape() {
        var savedRay: Ray? = null
        override fun localIntersect(localRay: Ray): Intersections? {
            savedRay = localRay
            return null
        }
        override fun localNormal(localPoint: Point) = point(localPoint.x, localPoint.y, localPoint.z)
    }
    @Test
    fun `The default transformation`() {
        val s = TestShape()
        assertEquals(Matrix.identity(), s.transform)
    }

    @Test
    fun `Assigning a transformation`() {
        val s = TestShape()

        s.transform = s.transform.translate(2, 3, 4)

        assertEquals(Matrix.translation(2, 3, 4), s.transform)
    }

    @Test
    fun `The default material`() {
        val s = TestShape()

        val m = s.material

        assertEquals(Material(), m)
    }

    @Test
    fun `Assigning a material`() {
        val s = TestShape()
        val m = Material()
        m.ambient = 1

        s.material = m

        assertEquals(m, s.material)
    }

    @Test
    fun `Intersecting a scaled shape with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = TestShape().apply {
            transform = transform.scale(2, 2, 2)
        }

        s.intersect(r)

        assertAll(
            { assertEquals(point(0, 0, -2.5), s.savedRay!!.origin) },
            { assertEquals(vector(0, 0, 0.5), s.savedRay!!.dir) }
        )
    }

    @Test
    fun `Intersecting a translated shape with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = TestShape().apply {
            transform = transform.translate(5, 0, 0)
        }

        s.intersect(r)

        assertAll(
            { assertEquals(point(-5, 0, -5), s.savedRay!!.origin) },
            { assertEquals(vector(0, 0, 1), s.savedRay!!.dir) }
        )
    }

    @Test
    fun `Computing the normal on a translated shape`() {
        val s = TestShape()

        s.transform = s.transform.translate(0, 1, 0)
        val n = s.normal(point(0, 1.70711, -0.70711))

        assertEquals(vector(0, 0.70711, -0.70711), n)
    }

    @Test
    fun `Computing the normal on a transformed shape`() {
        val s = TestShape().apply {
            transform = transform
                .rotateZ(PI/5)
                .scale(1, 0.5, 1)
        }

        val n = s.normal(point(0, sqrt(2.0)/2, -sqrt(2.0)/2))

        assertEquals(vector(0, 0.97014, -0.24254), n)
    }
}