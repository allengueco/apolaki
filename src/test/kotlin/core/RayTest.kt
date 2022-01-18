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

    @Test
    fun `Translating a ray`() {
        val r = Ray(point(1, 2, 3), vector(0, 1, 0))
        val m = Matrix.translation(3, 4, 5)

        val r2 = r.transform(m)

        assertAll(
            { assertEquals(point(4, 6, 8), r2.origin) },
            { assertEquals(vector(0, 1, 0), r2.dir) }
        )
    }

    @Test
    fun `Scaling a ray`() {
        val r = Ray(point(1, 2, 3), vector(0, 1, 0))
        val m = Matrix.scaling(2, 3, 4)

        val r2 = r.transform(m)

        assertAll(
            { assertEquals(point(2, 6, 12), r2.origin) },
            { assertEquals(vector(0, 3, 0), r2.dir) }
        )
    }
}
