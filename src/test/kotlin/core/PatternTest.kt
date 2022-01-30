package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PatternTest {
    private val black = color(0, 0, 0)
    val white = color(1, 1, 1)

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
}