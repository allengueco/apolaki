package core

import patterns.Stripe
import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import scene.Light
import scene.Material
import shape.Sphere
import kotlin.math.sqrt
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MaterialTest {
    private val m = Material()
    private val position = point(0, 0, 0)
    val s = Sphere()


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

        val result = m.lighting(light, s, position, eyeVector, normalVector)

        assertEquals(color(1.9, 1.9, 1.9), result)
    }

    @Test
    fun `Lighting with the eye between light and surface, eye offset 45deg`() {
        val eyeVector = vector(0, sqrt(2.0)/2, -sqrt(2.0)/2)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, -10), color(1, 1, 1))

        val result = m.lighting(light, s, position, eyeVector, normalVector)

        assertEquals(color(1.0, 1.0, 1.0), result)
    }

    @Test
    fun `Lighting with the eye opposite surface, light offset 45deg`() {
        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 10, -10), color(1, 1, 1))

        val result = m.lighting(light, s, position, eyeVector, normalVector)

        assertEquals(color(.7364, .7364, .7364), result)
    }

    @Test
    fun `Lighting with the eye in the path of the reflection vector`() {
        val eyeVector = vector(0, -sqrt(2.0)/2, -sqrt(2.0)/2)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 10, -10), color(1, 1, 1))

        val result = m.lighting(light, s, position, eyeVector, normalVector)

        assertEquals(color(1.6364, 1.6364, 1.6364), result)
    }

    @Test
    fun `Lighting with the light behind the surface`() {
        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, 10), color(1, 1, 1))

        val result = m.lighting(light, s, position, eyeVector, normalVector)

        assertEquals(color(0.1, 0.1, 0.1), result)
    }

    @Test
    fun `Lighting with the surface in shadow`() {
        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, -1), color(1, 1, 1))
        val inShadow = true

        val res = m.lighting(light, s, position, eyeVector, normalVector, inShadow)

        assertEquals(color(0.1, 0.1, 0.1), res)
    }

    @Test
    fun `Lighting with a pattern applied`() {
        val m = Material().apply {
            ambient = 1.0
            diffuse = 0.0
            specular = 0.0
            pattern = Stripe(color(1, 1, 1), color(0, 0, 0))
        }

        val eyeVector = vector(0, 0, -1)
        val normalVector = vector(0, 0, -1)
        val light = Light(point(0, 0, -10), color(1, 1, 1))

        val c1 = m.lighting(light, s, point(0.9, 0, 0,), eyeVector, normalVector)
        val c2 = m.lighting(light, s, point(1.1, 0, 0,), eyeVector, normalVector)

        assertEquals(color(1, 1, 1), c1)
        assertEquals(color(0, 0, 0), c2)
    }

    @Test
    fun `Reflectivity for the default material`() {
        val m = Material()

        assertEquals(0.0, m.reflective)
    }
}