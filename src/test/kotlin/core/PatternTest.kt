package core

import patterns.Checkers
import patterns.Gradient
import base.Pattern
import patterns.Ring
import patterns.Stripe
import core.Tuple.Companion.color
import core.Tuple.Companion.point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import shape.Sphere

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PatternTest {
    private val black = color(0, 0, 0)
    private val white = color(1, 1, 1)

    class TestPattern: Pattern() {
        override fun color(point: Point, c1: Color, c2: Color): Color {
            return color(0, 0, 0)
        }

        override fun get(point: Point): Color = color(point.x, point.y, point.z)

    }

    @Test
    fun `Creating a stripe pattern`() {
        val pattern = Stripe(white, black)

        assertEquals(white, pattern.a)
        assertEquals(black, pattern.b)
    }

    @Test
    fun `A stripe pattern is constant in y`() {
        val pattern = Stripe(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(white, pattern[point(0, 1, 0)]) },
            { assertEquals(white, pattern[point(0, 2, 0)]) }
        )
    }

    @Test
    fun `A stripe pattern is constant in z`() {
        val pattern = Stripe(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(white, pattern[point(0, 0, 1)]) },
            { assertEquals(white, pattern[point(0, 0, 2)]) }
        )
    }

    @Test
    fun `A stripe pattern alternates in x`() {
        val pattern = Stripe(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(white, pattern[point(0.9, 0, 0)]) },
            { assertEquals(black, pattern[point(1, 0, 0)]) },
            { assertEquals(black, pattern[point(-0.1, 0, 0)]) },
            { assertEquals(black, pattern[point(-1, 0, 0)]) },
            { assertEquals(white, pattern[point(-1.1, 0, 0)]) }
        )
    }

    @Test
    fun `Stripes with an object transformation`() {
        val obj = Sphere().apply {
            transform = transform.scale(2, 2, 2)
        }
        val pattern = Stripe(white, black)

        val c = pattern[obj, point(1.5, 0, 0)]

        assertEquals(white, c)
    }

    @Test
    fun `Stripes with a pattern transformation`() {
        val obj = Sphere()
        val pattern = Stripe(white, black).apply {
            transform = transform.scale(2, 2, 2)
        }

        val c = pattern[obj, point(1.5, 0, 0)]

        assertEquals(white, c)
    }

    @Test
    fun `Stripes with both an object and a pattern transformation`() {
        val obj = Sphere().apply {
            transform = transform.scale(2, 2, 2)
        }
        val pattern = Stripe(white, black).apply {
            transform = transform.translate(0.5, 0, 0)
        }

        val c = pattern[obj, point(2.5, 0, 0)]

        assertEquals(white, c)
    }
    @Test
    fun `The default pattern transformation`() {
        val pattern = TestPattern()

        assertEquals(Matrix.identity(), pattern.transform)
    }

    @Test
    fun `Assigning a transformation`() {
        val pattern = TestPattern()

        pattern.apply {
            transform = transform.translate(1, 2, 3)
        }

        assertEquals(Matrix.translation(1, 2, 3), pattern.transform)
    }

    @Test
    fun `A pattern with an object transformation`() {
        val shape = Sphere().apply {
            transform = transform.scale(2, 2, 2)
        }
        val pattern = TestPattern()

        val c = pattern[shape, point(2, 3, 4)]

        assertEquals(color(1, 1.5, 2), c)
    }

    @Test
    fun `A pattern with a pattern transformation`() {
        val shape = Sphere()
        val pattern = TestPattern(). apply {
            transform = transform.scale(2, 2, 2)
        }

        val c = pattern[shape, point(2, 3, 4)]

        assertEquals(color(1, 1.5, 2), c)
    }

    @Test
    fun `A pattern with both an object and a pattern transformation`() {
        val shape = Sphere().apply {
            transform = transform.scale(2, 2, 2)
        }
        val pattern = TestPattern().apply {
            transform = transform.translate(0.5, 1, 1.5)
        }

        val c = pattern[shape, point(2.5, 3, 3.5)]

        assertEquals(color(0.75, 0.5, 0.25), c)
    }

    @Test
    fun `A gradient linearly interpolates between colors`() {
        val pattern = Gradient(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(color(0.75, 0.75, 0.75), pattern[point(0.25, 0, 0)]) },
            { assertEquals(color(0.5, 0.5, 0.5), pattern[point(0.5, 0, 0)]) },
            { assertEquals(color(0.25, 0.25, 0.25), pattern[point(0.75, 0, 0)]) },
        )
    }

    @Test
    fun `A ring should extend in both x and z`() {
        val pattern = Ring(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(black, pattern[point(1, 0, 0)]) },
            { assertEquals(black, pattern[point(0, 0, 1)]) },
            { assertEquals(black, pattern[point(0.708, 0, 0.708)]) },
        )
    }

    @Test
    fun `Checkers should repeat in x`() {
        val pattern = Checkers(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(white, pattern[point(0.99, 0, 0)]) },
            { assertEquals(black, pattern[point(1.01, 0, 0)]) }
        )
    }

    @Test
    fun `Checkers should repeat in y`() {
        val pattern = Checkers(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(white, pattern[point(0, 0.99, 0)]) },
            { assertEquals(black, pattern[point(0, 1.01, 0)]) }
        )
    }

    @Test
    fun `Checkers should repeat in z`() {
        val pattern = Checkers(white, black)

        assertAll(
            { assertEquals(white, pattern[point(0, 0, 0)]) },
            { assertEquals(white, pattern[point(0, 0, 0.99)]) },
            { assertEquals(black, pattern[point(0, 0, 1.01)]) }
        )
    }
}