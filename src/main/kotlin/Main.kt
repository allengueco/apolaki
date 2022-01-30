import core.*
import core.Tuple.Companion.color
import core.Tuple.Companion.point
import core.Tuple.Companion.vector
import kotlin.math.PI

fun main(args: Array<String>) {
    `Putting It Together Ch9`()
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
    val canvas = Canvas(100, 100, color(0, 0, 0))

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

fun `Putting It Together Ch6`() {
    val canvasPixels = 250
    val canvas = Canvas(canvasPixels, canvasPixels, color(0, 0, 0))

    val rayOrigin = point(0, 0, -5)
    val wallZ = 10.0
    val wallSize = 7.0

    val pixelSize = wallSize / canvasPixels

    val half = wallSize / 2

    val sphere = Sphere()
    sphere.material.color = color(1, 0.2, 1)

    val light = Light(point(-10, 10, -10), color(1, 1, 1))

    for (y in 0 until canvasPixels - 1) {
        val worldY = half - pixelSize * y
        for (x in 0 until canvasPixels - 1) {
            val worldX = -half + pixelSize * x

            val pos = point(worldX, worldY, wallZ)

            val ray = Ray(rayOrigin, (pos - rayOrigin).normalize())
            val xs = sphere.intersect(ray)

            xs?.let {
                it.hit()?.let { h ->
                    {
                        val point = ray.at(h.t)
                        val normal = h.obj.normal(point)
                        val eye = -ray.dir
                        val color = h.obj.material.lighting(light, h.obj, point, eye, normal)
                        canvas[x, y] = color
                    }
                }
            }
        }
    }

    canvas.saveCanvasToFile("pit_ch6.ppm")
}

fun `Putting It Together Ch7`() {
    val floor = Sphere().apply {
        transform = Matrix.scaling(10, 0.01, 10)
        material = Material().apply {
            color = color(1, 0.9, 0.9)
            specular = 0
        }
    }

    val leftWall = Sphere().apply {
        transform =
            Matrix.translation(0, 0, 5) *
                    Matrix.rotationY(-PI / 4) *
                    Matrix.rotationX(PI / 2) *
                    Matrix.scaling(10, 0.01, 10)
        material = floor.material
    }

    val rightWall = Sphere().apply {
        transform =
            Matrix.translation(0, 0, 5) *
                    Matrix.rotationY(PI / 4) *
                    Matrix.rotationX(PI / 2) *
                    Matrix.scaling(10, 0.01, 10)
        material = floor.material
    }

    val middle = Sphere().apply {
        transform = transform.translate(-0.5, 1, 0.5)
        material = Material().apply {
            color = color(0.1, 1, 0.5)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val right = Sphere().apply {
        transform =
            Matrix.translation(1.5, 0.5, -0.5) *
                    Matrix.scaling(0.5, 0.5, 0.5)
        material = Material().apply {
            color = color(0.5, 1, 0.1)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val left = Sphere().apply {
        transform = Matrix.translation(-1.5, 0.33, -0.75) *
                Matrix.scaling(.33, 0.33, 0.33)
        material = Material().apply {
            color = color(1, 0.8, 0.1)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val objects = mutableListOf<Shape>(
        floor, leftWall, rightWall, left, right, middle
    )
    val world = World(
        light = Light(point(-10, 10, -10), color(1, 1, 1)),
        objects = objects
    )

    val camera = Camera(500, 250, PI / 5).apply {
        transform = Matrix.view(
            from = point(0, 1.5, -10),
            to = point(0, 1, 0),
            up = vector(0, 1, 0))
    }

    world.render(camera).also { it.saveCanvasToFile("pit_ch7.ppm") }
}

/**
 * Same as Ch7 but this one has shadows now
 */
fun `Putting It Together Ch8`() {
    val floor = Sphere().apply {
        transform = Matrix.scaling(10, 0.01, 10)
        material = Material().apply {
            color = color(1, 0.9, 0.9)
            specular = 0
        }
    }

    val leftWall = Sphere().apply {
        transform =
            Matrix.translation(0, 0, 5) *
                    Matrix.rotationY(-PI / 4) *
                    Matrix.rotationX(PI / 2) *
                    Matrix.scaling(10, 0.01, 10)
        material = floor.material
    }

    val rightWall = Sphere().apply {
        transform =
            Matrix.translation(0, 0, 5) *
                    Matrix.rotationY(PI / 4) *
                    Matrix.rotationX(PI / 2) *
                    Matrix.scaling(10, 0.01, 10)
        material = floor.material
    }

    val middle = Sphere().apply {
        transform = transform.translate(-0.5, 1, 0.5)
        material = Material().apply {
            color = color(0.1, 1, 0.5)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val right = Sphere().apply {
        transform =
            Matrix.translation(1.5, 0.5, -0.5) *
                    Matrix.scaling(0.5, 0.5, 0.5)
        material = Material().apply {
            color = color(0.5, 1, 0.1)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val left = Sphere().apply {
        transform = Matrix.translation(-1.5, 0.33, -0.75) *
                Matrix.scaling(.33, 0.33, 0.33)
        material = Material().apply {
            color = color(1, 0.8, 0.1)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val objects = mutableListOf<Shape>(
        floor, leftWall, rightWall, left, right, middle
    )
    val world = World(
        light = Light(point(-10, 10, -10), color(1, 1, 1)),
        objects = objects
    )

    val camera = Camera(500, 250, PI / 5).apply {
        transform = Matrix.view(
            from = point(0, 1.5, -10),
            to = point(0, 1, 0),
            up = vector(0, 1, 0))
    }

    world.render(camera).also { it.saveCanvasToFile("pit_ch8.ppm") }
}

fun `Putting It Together Ch9`() {
    val floor = Plane()

    val backdrop = Plane().apply {
        transform = transform
            .rotateX(PI/2)
            .translate(0, 0, 10)
    }

    val middle = Sphere().apply {
        transform = transform.translate(-0.5, 1, 0.5)
        material = Material().apply {
            color = color(0.1, 1, 0.5)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val right = Sphere().apply {
        transform =
            Matrix.translation(1.5, 0.5, -0.5) *
                    Matrix.scaling(0.5, 0.5, 0.5)
        material = Material().apply {
            color = color(0.5, 1, 0.1)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val left = Sphere().apply {
        transform = Matrix.translation(-1.5, 0.33, -0.75) *
                Matrix.scaling(.33, 0.33, 0.33)
        material = Material().apply {
            color = color(1, 0.8, 0.1)
            diffuse = 0.7
            specular = 0.3
        }
    }

    val objects = mutableListOf(
        floor, backdrop, left, right, middle
    )
    val world = World(
        light = Light(point(-10, 10, -10), color(1, 1, 1)),
        objects = objects
    )

    val camera = Camera(500, 250, PI / 5).apply {
        transform = Matrix.view(
            from = point(0, 2.5, -5.50),
            to = point(0, 1, 0),
            up = vector(0, 1, 0))
    }

    world.render(camera).also { it.saveCanvasToFile("pit_ch9_backdrop.ppm") }
}