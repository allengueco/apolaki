package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import scene.Camera
import scene.World
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CameraTest {
    @Test
    fun `Constructing a camera`() {
        val height = 160
        val width = 120
        val fov = PI / 2

        val c = Camera(height, width, fov)

        assertAll(
            { assertEquals(160, c.hSize) },
            { assertEquals(120, c.vSize) },
            { assertEquals(PI / 2, c.fov, 0.0001) },
            { assertEquals(Matrix.identity(), c.transform) }
        )
    }

    @Test
    fun `The pixel size for a horizontal canvas`() {
        val c = Camera(200, 125, PI/2)
        assertTrue { Utils.equals(0.01, c.pixelSize) }
    }

    @Test
    fun `The pixel size for a vertical canvas`() {
        val c = Camera(125, 200, PI/2)
        assertTrue { Utils.equals(0.01, c.pixelSize) }
    }

    @Test
    fun `Constructing a ray through the center of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.cast(100, 50)

        assertAll(
            { assertEquals(point(0, 0, 0), r.origin) },
            { assertEquals(vector(0, 0, -1), r.dir) }
        )
    }

    @Test
    fun `Constructing a ray through a corner of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.cast(0, 0)

        assertAll(
            { assertEquals(point(0, 0, 0), r.origin) },
            { assertEquals(vector(0.66519, 0.33259, -0.66851), r.dir) }
        )
    }

    @Test
    fun `Constructing a ray when the camera is transformed`() {
        val c = Camera(201, 101, PI / 2).apply {
            transform = transform.translate(0, -2, 5).rotateY(PI/4)
        }
        val r = c.cast(100, 50)

        assertAll(
            { assertEquals(point(0, 2, -5), r.origin) },
            { assertEquals(vector(sqrt(2.0)/2, 0, -sqrt(2.0)/2), r.dir) }
        )
    }

    @Test
    fun `Rendering a world with a camera`() {
        val w = World()
        val from = point(0, 0, -5)
        val to = point(0, 0, 0)
        val up = vector(0, 1, 0)
        val c = Camera(11, 11, PI/2).apply {
            transform = Matrix.view(from, to, up)
        }

        val image = w.render(c)

        assertEquals(color(0.38066, 0.47583, 0.2855), image[5, 5])

    }
}