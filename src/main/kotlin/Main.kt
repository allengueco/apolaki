import core.*
import core.Tuple.*
import core.Tuple.Companion.color
import core.Tuple.Companion.vector
import core.Tuple.Companion.point

fun main(args: Array<String>) {
    `Putting It Together Ch2`()
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