package core

import core.Tuple.Companion.point
import kotlin.math.tan

class Camera(val hSize: Int, val vSize: Int, val fov: Double) {
    var transform = Matrix.identity()
    private val halfWidth: Double
    private val halfHeight: Double
    val pixelSize: Double
    init {
        val halfView = tan(fov / 2)
        val aspectRatio = hSize.toDouble() / vSize.toDouble()
        if (aspectRatio >= 1.0) {
            halfWidth = halfView
            halfHeight = halfView / aspectRatio
        }
        else {
            halfWidth = halfView * aspectRatio
            halfHeight = halfView
        }
        pixelSize = (halfWidth * 2) / hSize
    }
    fun cast(x: Int, y: Int): Ray {
        val xOffset = (x + .5) * pixelSize
        val yOffset = (y + .5) * pixelSize

        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset

        val pixel = transform.inverse() * point(worldX, worldY, -1)

        val origin = transform.inverse() * point(0, 0, 0)
        val dir = (pixel - origin).normalize()

        return Ray(origin, dir)
    }

    override fun toString(): String {
        return "Camera(hSize=$hSize, vSize=$vSize, fov=$fov, halfWidth=$halfWidth, halfHeight=$halfHeight, pixelSize=$pixelSize)"
    }
}
