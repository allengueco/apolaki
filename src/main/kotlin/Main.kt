import core.*
import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import kotlin.math.PI

fun main(args: Array<String>) {
    `Putting It Together Ch5`()
}

fun `Putting It Together Ch1`() {
    data class Projectile(val position: Point, val velocity: Vector)
    data class Environment(val gravity: Vector, val wind: Vector)

    fun tick(env: Environment, proj: Projectile): Projectile {
        val position = proj.position + proj.velocity
        val velocity = proj.velocity + env.gravity + env.wind
        return Projectile(position, velocity)
    }

    var p = Projectile(point(0, 1, 0), vector(1, 1, 0).normalize())
    val e = Environment(vector(.0, -0.1, .0), vector(-0.01, 0.0, 0.0))

    while (true) {
        p = tick(e, p)
        println(p)
        if (p.position.y <= 0) break
    }
}

fun `Putting It Together Ch2`() {
    data class Projectile(val position: Point, val velocity: Vector)
    data class Environment(val gravity: Vector, val wind: Vector)

    fun tick(env: Environment, proj: Projectile): Projectile {
        val position = proj.position + proj.velocity
        val velocity = proj.velocity + env.gravity + env.wind
        return Projectile(position, velocity)
    }

    val start = point(0, 1, 0)
    val velocity = vector(1.0, 1.8, 0.0).normalize() * 11.25
    var p = Projectile(start, velocity)

    val gravity = vector(0.0, -0.1, 0.0)
    val wind = vector(-0.01, 0.0, 0.0)
    val e = Environment(gravity, wind)

    val canvas = Canvas(900, 550, color(0, 0, 0))

    while (true) {
        p = tick(e, p)
        val currentY = canvas.height - p.position.y.toInt()
        val currentX = canvas.width - p.position.x.toInt()
        canvas[currentX, currentY] = color(1, 0, 0)
        if (p.position.y <= 0) break
    }

    canvas.saveCanvasToFile("pit_ch2.ppm")


}

fun `Putting It Together Ch4`() {
    data class Circle(val radius: Double)

    val canvas = Canvas(250, 250, color(0, 0, 0))

    fun Canvas.drawClock() {
        val circle = Circle(3.0 / 8.0 * this.width.toDouble())
        val center = point(this.width / 2.0, 0.0, this.height / 2.0)
        fun getHourTransform(hour: Int) = Matrix.rotationY(hour * PI / 6)

        val twelve = point(0, 0, 1)
        (0..12).forEach { hour ->
            var hourPoint = getHourTransform(hour) * twelve
            hourPoint *= point(circle.radius, 1.0, circle.radius)
            hourPoint += point(center.x, 0.0, center.z)
            val (x, y) = (hourPoint.x to hourPoint.z)
            this[x.toInt(), y.toInt()] = color(1, 0, 0)
        }
    }

    canvas.drawClock()
    canvas.saveCanvasToFile("pit_ch4.ppm")
}

fun `Putting It Together Ch5`() {
    val canvas = Canvas(100,100, color(0, 0, 0))

    val rayOrigin = point(0, 0, -5)
    val wallZ = 10.0
    val wallSize = 7.0

    val canvasPixels = 100
    val pixelSize = wallSize / canvasPixels

    val half = wallSize / 2

    val sphere = Sphere()
    sphere.transform = sphere.transform.shear(1 to 0, 0 to 0, 0 to 0).scale(0.5, 1, 1)
    val sphereColor = color(1, 0, 0)

    for (y in 0 until canvasPixels - 1) {
        val worldY = half - pixelSize * y
        for (x in 0 until canvasPixels - 1) {
            val worldX = -half + pixelSize * x

            val pos = point(worldX, worldY, wallZ)

            val r = Ray(rayOrigin, (pos - rayOrigin).normalize())
            val xs = sphere.intersect(r)

            xs?.let { canvas[x, y] = sphereColor }
        }
    }

    canvas.saveCanvasToFile("pit_ch5.ppm")
}