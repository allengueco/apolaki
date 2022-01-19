package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class LightTest {
    @Test
    fun `A point light has a position and intensity`() {
        val intensity = color(1, 1, 1)
        val position = point(0, 0, 0)

        val light = Light(position, intensity)

        assertAll(
            { assertEquals(position, light.position) },
            { assertEquals(intensity, light.intensity) }
        )
    }

}