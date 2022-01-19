package core

import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.sqrt
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MaterialTest {
    private val m = Material()
    private val position = point(0, 0, 0)


    @Test
    fun `The default material`() {
        val m = Material()

        Assertions.assertAll(
            { Assertions.assertEquals(color(1, 1, 1), m.color) },
            { Assertions.assertEquals(0.1, m.ambient) },
            { Assertions.assertEquals(0.9, m.diffuse) },
            { Assertions.assertEquals(0.9, m.specular) },
            { Assertions.assertEquals(200.0, m.shininess) },
        )
    }

    @Test
    fun `Lighting with the eye between the light and the surface`() {
        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, -10), color(1, 1, 1))

        val result = m.lighting(light, position, eyeVector, normalVector)

        assertEquals(color(1.9, 1.9, 1.9), result)
    }

    @Test
    fun `Lighting with the eye between light and surface, eye offset 45deg`() {
        val eyeVector = vector(0, sqrt(2.0)/2, -sqrt(2.0)/2)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, -10), color(1, 1, 1))

        val result = m.lighting(light, position, eyeVector, normalVector)

        assertEquals(color(1.0, 1.0, 1.0), result)
    }

    @Test
    fun `Lighting with the eye opposite surface, light offset 45deg`() {
        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 10, -10), color(1, 1, 1))

        val result = m.lighting(light, position, eyeVector, normalVector)

        assertEquals(color(.7364, .7364, .7364), result)
    }

    @Test
    fun `Lighting with the eye in the path of the reflection vector`() {
        val eyeVector = vector(0, -sqrt(2.0)/2, -sqrt(2.0)/2)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 10, -10), color(1, 1, 1))

        val result = m.lighting(light, position, eyeVector, normalVector)

        assertEquals(color(1.6364, 1.6364, 1.6364), result)
    }

    @Test
    fun `Lighting with the light behind the surface`() {
        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, 10), color(1, 1, 1))

        val result = m.lighting(light, position, eyeVector, normalVector)

        assertEquals(color(0.1, 0.1, 0.1), result)
    }
}