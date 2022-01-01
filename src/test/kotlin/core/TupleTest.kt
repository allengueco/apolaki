package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import kotlin.math.sqrt

internal class TupleTest {
    @Test
    fun `A Tuple with w = 1,0 is a point`() {
        val a = Tuple(4.3, -4.2, 3.1, 1.0)
        assertAll(
            Executable { assertEquals(4.3, a.x) },
            Executable { assertEquals(-4.2, a.y) },
            Executable { assertEquals(3.1, a.z) },
            Executable { assertEquals(1.0, a.w) },
            Executable { assertTrue { a.isPoint() } },
            Executable { assertTrue { !a.isVector() } }
        )
    }

    @Test
    fun `A Tuple with w = 0,0 is a vector`() {
        val a = Tuple(4.3, -4.2, 3.1, 0.0)
        assertAll(
            Executable { assertEquals(4.3, a.x) },
            Executable { assertEquals(-4.2, a.y) },
            Executable { assertEquals(3.1, a.z) },
            Executable { assertEquals(0.0, a.w) },
            Executable { assertTrue { !a.isPoint() } },
            Executable { assertTrue { a.isVector() } }
        )
    }

    @Test
    fun `'point()' creates Tuples with w = 1,0`() {
        val p = point(4.0, -4.0, 3.0)
        assertEquals(Tuple(4.0, -4.0, 3.0, 1.0), p)
    }

    @Test
    fun `'vector()' creates tuples with w = 0,0`() {
        val v = vector(4.0, -4.0, 3.0)
        assertEquals(Tuple(4.0, -4.0, 3.0, 0.0), v)
    }

    @Test
    fun `Adding two tuples`() {
        val a1 = Tuple(3.0, -2.0, 5.0, 1.0)
        val a2 = Tuple(-2.0, 3.0, 1.0, 0.0)

        assertEquals(Tuple(1.0, 1.0, 6.0, 1.0), a1 + a2)
    }

    @Test
    fun `Subtracting two points`() {
        val p1 = point(3.0, 2.0, 1.0)
        val p2 = point(5.0, 6.0, 7.0)

        assertEquals(vector(-2.0, -4.0, -6.0), p1 - p2)
    }

    @Test
    fun `Subtracting a vector from a point`() {
        val p = point(3.0, 2.0, 1.0)
        val v = vector(5.0, 6.0, 7.0)

        assertEquals(point(-2.0, -4.0, -6.0), p - v)
    }

    @Test
    fun `Subtracting two vectors`() {
        val v1 = vector(3.0, 2.0, 1.0)
        val v2 = vector(5.0, 6.0, 7.0)

        assertEquals(vector(-2.0, -4.0, -6.0), v1 - v2)
    }

    @Test
    fun `Subtracting a vector from the zero vector`() {
        val zero = vector(0.0, 0.0, 0.0)
        val v = vector(1.0, -2.0, 3.0)

        assertEquals(vector(-1.0, 2.0, -3.0), zero - v)
    }

    @Test
    fun `Negating a tuple`() {
        val a = Tuple(1.0, -2.0, 3.0, -4.0)

        assertEquals(Tuple(-1.0, 2.0, -3.0, 4.0), -a)
    }

    @Test
    fun `Multiplying a tuple by a scalar`() {
        val a = Tuple(1.0, -2.0, 3.0, -4.0)

        assertEquals(Tuple(3.5, -7.0, 10.5, -14.0), a * 3.5)
    }

    @Test
    fun `Multiplying a tuple by a fraction`() {
        val a = Tuple(1.0, -2.0, 3.0, -4.0)

        assertEquals(Tuple(0.5, -1.0, 1.5, -2.0), a * 0.5)
    }

    @Test
    fun `Dividing a tuple by a scalar`() {
        val a = Tuple(1.0, -2.0, 3.0, -4.0)

        assertEquals(Tuple(0.5, -1.0, 1.5, -2.0), a / 2.0)
    }

    @Test
    fun `Computing the magnitude of vector(1, 0, 0)`() {
        val v = vector(1.0, 0.0, 0.0)

        assertEquals(1.0, v.magnitude())
    }

    @Test
    fun `Computing the magnitude of vector(0, 1, 0)`() {
        val v = vector(1.0, 0.0, 0.0)

        assertEquals(1.0, v.magnitude())
    }

    @Test
    fun `Computing the magnitude of vector(0, 0, 1)`() {
        val v = vector(0.0, 0.0, 1.0)

        assertEquals(1.0, v.magnitude())
    }

    @Test
    fun `Computing the magnitude of vector(1, 2, 3)`() {
        val v = vector(1.0, 2.0, 3.0)

        assertEquals(sqrt(14.0), v.magnitude())
    }

    @Test
    fun `Computing the magnitude of vector(-1, -2, -3)`() {
        val v = vector(-1.0, -2.0, -3.0)

        assertEquals(sqrt(14.0), v.magnitude())
    }

    @Test
    fun `Normalizing vector(4, 0, 0) gives (1, 0, 0)`() {
        val v = vector(4.0, 0.0, 0.0)

        assertEquals(vector(1.0, 0.0, 0.0), v.normalize())
    }

    @Test
    fun `Normalizing vector(1, 2, 3)`() {
        val v = vector(1.0, 2.0, 3.0)

        assertEquals(vector(1 / sqrt(14.0), 2 / sqrt(14.0), 3 / sqrt(14.0)), v.normalize())
    }

    @Test
    fun `The magnitude of a normalized vector`() {
        val v = vector(1.0, 2.0, 3.0)

        assertEquals(1.0, v.normalize().magnitude())
    }

    @Test
    fun `The dot product of two tuples`() {
        val a = vector(1.0, 2.0, 3.0)
        val b = vector(2.0, 3.0, 4.0)

        assertEquals(20.0, a.dot(b))
    }

    @Test
    fun `The cross product of two vectors`() {
        val a = vector(1.0, 2.0, 3.0)
        val b = vector(2.0, 3.0, 4.0)

        assertAll(
            Executable { assertEquals(vector(-1.0, 2.0, -1.0), a.cross(b)) },
            Executable { assertEquals(vector(1.0, -2.0, 1.0), b.cross(a)) }
        )
    }

    @Test
    fun `Colors are (red, green, blue) tuples`() {
        val c = color(-0.5, 0.4, 1.7)
        assertAll(
            Executable { assertEquals(-0.5, c.red) },
            Executable { assertEquals(0.4, c.green) },
            Executable { assertEquals(1.7, c.blue) }
        )
    }

    @Test
    fun `Adding colors`() {
        val c1 = color(0.9, 0.6, .75)
        val c2 = color(0.7, 0.1, .25)

        assertEquals(color(1.6, 0.7, 1.0), c1 + c2)
    }

    @Test
    fun `Subtracting colors`() {
        val c1 = color(0.9, 0.6, .75)
        val c2 = color(0.7, 0.1, .25)

        assertEquals(color(0.2, 0.5, 0.5), c1 - c2)
    }

    @Test
    fun `Multiplying a color by a scalar`() {
        val c1 = color(0.2, 0.3, .4)

        assertEquals(color(0.4, 0.6, .8), c1 * 2.0)
    }

    @Test
    fun `Multiplying colors`() {
        val c1 = color(1.0, 0.2, .4)
        val c2 = color(0.9, 1.0, .1)

        assertEquals(color(.9, 0.2, .04), c1 * c2)
    }

}