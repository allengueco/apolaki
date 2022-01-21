package core

import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class TransformationTest {
    @Test
    fun `Multiplying by a translation matrix`() {
        val transform = Matrix.translation(5, -3, 2)

        val p = point(-3, 4, 5)

        assertEquals(point(2, 1, 7), transform * p)
    }

    @Test
    fun `Multiplying by the inverse of a translation matrix`() {
        val transform = Matrix.translation(5, -3, 2)

        val inv = transform.inverse()

        val p = point(-3, 4, 5)

        assertEquals(point(-8, 7, 3), inv * p)
    }

    @Test
    fun `Translation does not affect vectors`() {
        val transform = Matrix.translation(5, -3, 2)

        val v = vector(-3, 4, 5)

        assertEquals(v, transform * v)
    }

    @Test
    fun `A scaling matrix applied to a point`() {
        val transform = Matrix.scaling(2, 3, 4)
        val p = point(-4, 6, 8)
        assertEquals(point(-8, 18, 32), transform * p)
    }

    @Test
    fun `A scaling matrix applied to a vector`() {
        val transform = Matrix.scaling(2, 3, 4)
        val v = vector(-4, 6, 8)
        assertEquals(vector(-8, 18, 32), transform * v)
    }

    @Test
    fun `Multiplying by the inverse of a scaling matrix`() {
        val transform = Matrix.scaling(2, 3, 4)

        val inv = transform.inverse()

        val v = vector(-4, 6, 8)

        assertEquals(vector(-2, 2, 2), inv * v)
    }

    @Test
    fun `Reflection is scaling by a negative value`() {
        val transform = Matrix.scaling(-1, 1, 1)
        val p = point(2, 3, 4)
        assertEquals(point(-2, 3, 4), transform * p)
    }

    @Test
    fun `Rotating a point around the x axis`() {
        val p = point(0, 1, 0)
        val halfQuarter = Matrix.rotationX(PI / 4)
        val fullQuarter = Matrix.rotationX(PI / 2)

        assertAll(
            { assertEquals(point(0.0, sqrt(2.0) / 2, sqrt(2.0) / 2), halfQuarter * p) },
            { assertEquals(point(0, 0, 1), fullQuarter * p) }
        )
    }

    @Test
    fun `The inverse of an x-rotation rotates in the opposite direction`() {
        val p = point(0, 1, 0)
        val halfQuarter = Matrix.rotationX(PI / 4)
        val inv = halfQuarter.inverse()

        assertEquals(point(0.0, sqrt(2.0) / 2, -sqrt(2.0) / 2), inv * p)
    }

    @Test
    fun `Rotating a point around the y axis`() {
        val p = point(0, 0, 1)
        val halfQuarter = Matrix.rotationY(PI / 4)
        val fullQuarter = Matrix.rotationY(PI / 2)

        assertAll(
            { assertEquals(point(sqrt(2.0) / 2, 0.0, sqrt(2.0) / 2), halfQuarter * p) },
            { assertEquals(point(1, 0, 0), fullQuarter * p) }
        )
    }

    @Test
    fun `Rotating a point around the z axis`() {
        val p = point(0, 1, 0)
        val halfQuarter = Matrix.rotationZ(PI / 4)
        val fullQuarter = Matrix.rotationZ(PI / 2)

        assertAll(
            { assertEquals(point(-sqrt(2.0) / 2, sqrt(2.0) / 2, 0.0), halfQuarter * p) },
            { assertEquals(point(-1, 0, 0), fullQuarter * p) }
        )
    }

    @Test
    fun `A shearing transformation moves x in proportion to y`() {
        val transform = Matrix.shearing(1 to 0, 0 to 0, 0 to 0)
        val p = point(2, 3, 4)

        assertEquals(point(5, 3, 4), transform * p)
    }

    @Test
    fun `A shearing transformation moves x in proportion to z`() {
        val transform = Matrix.shearing(0 to 1, 0 to 0, 0 to 0)
        val p = point(2, 3, 4)

        assertEquals(point(6, 3, 4), transform * p)
    }

    @Test
    fun `A shearing transformation moves y in proportion to x`() {
        val transform = Matrix.shearing(0 to 0, 1 to 0, 0 to 0)
        val p = point(2, 3, 4)

        assertEquals(point(2, 5, 4), transform * p)
    }

    @Test
    fun `A shearing transformation moves y in proportion to z`() {
        val transform = Matrix.shearing(0 to 0, 0 to 1, 0 to 0)
        val p = point(2, 3, 4)

        assertEquals(point(2, 7, 4), transform * p)
    }

    @Test
    fun `A shearing transformation moves z in proportion to x`() {
        val transform = Matrix.shearing(0 to 0, 0 to 0, 1 to 0)
        val p = point(2, 3, 4)

        assertEquals(point(2, 3, 6), transform * p)
    }

    @Test
    fun `A shearing transformation moves z in proportion to y`() {
        val transform = Matrix.shearing(0 to 0, 0 to 0, 0 to 1)
        val p = point(2, 3, 4)

        assertEquals(point(2, 3, 7), transform * p)
    }

    @Test
    fun `Individual transformations are applied in sequence`() {
        val p = point(1, 0, 1)
        val A = Matrix.rotationX(PI / 2)
        val B = Matrix.scaling(5, 5, 5)
        val C = Matrix.translation(10, 5, 7)

        val p2 = A * p
        assertEquals(point(1, -1, 0), p2)

        val p3 = B * p2
        assertEquals(point(5, -5, 0), p3)

        val p4 = C * p3
        assertEquals(point(15, 0, 7), p4)
    }

    @Test
    fun `Chained transformations must be applied in reverse order`() {
        val p = point(1, 0, 1)
        val A = Matrix.rotationX(PI / 2)
        val B = Matrix.scaling(5, 5, 5)
        val C = Matrix.translation(10, 5, 7)

        val T = C * B * A
        assertEquals(point(15, 0, 7), T * p)

        val transform = Matrix.identity()
            .rotateX(PI / 2)
            .scale(5, 5, 5)
            .translate(10, 5, 7)
        assertEquals(point(15, 0, 7), transform * p)

        val transformPipe = Matrix.identity().pipe(
            A, B, C
        )
        assertEquals(point(15, 0, 7), transformPipe * p)
    }

    @Test
    fun `The transformation matrix for the default orientation`() {
        val from = point(0, 0, 0)
        val to = point(0, 0, -1)
        val up = vector(0, 1, 0)

        val t = Matrix.view(from, to, up)

        assertEquals(Matrix.identity(), t)
    }

    @Test
    fun `A view transformation matrix looking in positive z direction`() {
        val from = point(0, 0, 0)
        val to = point(0, 0, 1)
        val up = vector(0, 1, 0)

        val t = Matrix.view(from, to, up)

        assertEquals(Matrix.scaling(-1, 1, -1), t)
    }

    @Test
    fun `Thew view transformation moves the world`() {
        val from = point(0, 0, 8)
        val to = point(0, 0, 0)
        val up = vector(0, 1, 0)

        val t = Matrix.view(from, to, up)

        assertEquals(Matrix.translation(0, 0, -8), t)
    }

    @Test
    fun `An arbitrary view transformation`() {
        val from = point(1, 3, 2)
        val to = point(4, -2, 8)
        val up = vector(1, 1, 0)

        val t = Matrix.view(from, to, up)

        val expected = Matrix() {
            listOf(
                listOf(-0.50709, 0.50709, 0.67612, -2.36643),
                listOf(0.76772, 0.60609, 0.12122, -2.82843),
                listOf(-0.35857, 0.59761, -0.71714, 0.00000),
                listOf(0.00000, 0.00000, 0.00000, 1.00000)
            )
        }

        assertEquals(expected, t)
    }
}