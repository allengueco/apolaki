package core

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class IntersectionTest {
    /**
     * Helper method that must return a sphere that is unique each time
     */
    private fun sphere() = Sphere(Random.nextDouble(10.0))
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
}