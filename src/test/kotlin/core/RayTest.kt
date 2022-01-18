package core


import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RayTest {
    @Test
    fun `Creating and querying a ray`() {
        val origin = point(1, 2, 3)
        val dir = vector(4, 5, 6)

        val ray = Ray(origin, dir)
        assertAll(
            { assertEquals(origin, ray.origin) },
            { assertEquals(dir, ray.dir) }
        )
    }

    @Test
    fun `Computing a point from a distance`() {
        val ray = Ray(point(2, 3, 4), vector(1, 0, 0))

        assertAll(
            { assertEquals(point(2, 3, 4), ray.at(0)) },
            { assertEquals(point(3, 3, 4), ray.at(1)) },
            { assertEquals(point(1, 3, 4), ray.at(-1)) },
            { assertEquals(point(4.5, 3.0, 4.0), ray.at(2.5)) },
        )
    }
}
