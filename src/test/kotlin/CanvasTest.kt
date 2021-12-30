import core.Canvas
import core.Tuple.Companion.color
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import kotlin.test.assertEquals

class CanvasTest {
    @Test
    fun `Creating a canvas`() {
        val c = Canvas(10, 20)

        assertAll(
            Executable { assertEquals(10, c.width) },
            Executable { assertEquals(20, c.height) },
            Executable { assertTrue(c.pixels().all { it == color(0, 0, 0) }) }
        )
    }

    @Test
    fun `Writing pixels to a canvas`() {
        val c = Canvas(10, 20)
        val red = color(1, 0, 0)

        c[2, 3] = red

        assertEquals(red, c[2, 3])
    }

    @Test
    fun `Constructing the PPM header`() {
        val c = Canvas(5, 3)
        val lines = c.ppm().split("\n")

        assertEquals("P3\n5 3\n255\n", lines.take(3).joinToString("\n", postfix = "\n"))
    }

    @Test
    fun `Constructing the PPM pixel data`() {
        val c = Canvas(5, 3)
        val c1 = color(1.5, .0, .0)
        val c2 = color(.0, .5, .0)
        val c3 = color(-0.5, .0, 1.0)

        c[0, 0] = c1
        c[2, 1] = c2
        c[4, 2] = c3

        val expectedLines = """
                255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
                0 0 0 0 0 0 0 0 0 0 0 0 0 0 255
        """.trimIndent()

        val lines = c.ppm().split("\n").drop(3).take(3)

        assertEquals(expectedLines.split("\n"), lines)
    }

    @Test
    fun `PPM files are terminated by a newline character`() {
        val c = Canvas(5, 3)
        val ppm = c.ppm()

        assertEquals('\n', ppm[ppm.lastIndex])
    }
}