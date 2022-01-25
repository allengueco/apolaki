package core

import core.Tuple.Companion.point
import kotlin.math.tan

class Camera(val height: Int, val width: Int, val fov: Double   ) {
    var transform = Matrix.identity()
    private val halfWidth: Double
    private val halfHeight: Double
    val pixelSize: Double
    init {
        val aspectRatio = height / width
        val halfView = tan(fov / 2)
        if (aspectRatio >= 1) {
            halfWidth = halfView
            halfHeight = halfView / aspectRatio
        }
        else {
            halfWidth = halfView * aspectRatio
            halfHeight = halfView
        }
        pixelSize = (halfWidth * 2) / height
    }
    fun castRay(x: Int, y: Int): Ray {
        val xOffset = (x + .5) * pixelSize
        val yOffset = (y + .5) * pixelSize

        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset

        val pixel = transform.inverse() * point(worldX, worldY, -1)

        val origin = transform.inverse() * point(0, 0, 0)
        val dir = (pixel - origin).normalize()

        return Ray(origin, dir)
    }
}
