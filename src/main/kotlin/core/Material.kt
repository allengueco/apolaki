package core

import core.Tuple.Companion.color
import kotlin.math.pow

data class Material(
    var color: Tuple = color(1, 1, 1),
    var ambient: Number = 0.1,
    var diffuse: Number = 0.9,
    var specular: Number = 0.9,
    var shininess: Number = 200.0,
) {
    fun lighting(
        light: Light,
        position: Tuple,
        eyeVector: Tuple,
        normalVector: Tuple,
        inShadow: Boolean = false,
    ): Tuple {
        val effectiveColor = color * light.intensity

        val lightVector = (light.position - position).normalize()

        val amb = effectiveColor * ambient.toDouble()

        if (inShadow) {
            return amb
        }

        val lightDotNormal = lightVector.dot(normalVector)
        val dif = if (lightDotNormal < 0.0) color(0, 0, 0) else {
            effectiveColor * diffuse.toDouble() * lightDotNormal
        }
        val spec = with(color(0, 0, 0)) {
            val reflectVector = -lightVector.reflect(normalVector)
            val reflectDotEye = reflectVector.dot(eyeVector)
            if (reflectDotEye > 0.0) {
                val factor = reflectDotEye.pow(shininess.toDouble())
                light.intensity * specular.toDouble() * factor
            } else {
                this
            }
        }
        return amb + dif + spec
    }
}
