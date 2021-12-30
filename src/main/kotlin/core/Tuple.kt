package core

import kotlin.math.sqrt

data class Tuple(
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double
) {
    fun isPoint() = this.w == 1.0
    fun isVector() = this.w == 0.0

    /**
     * If I'm too lazy to type the decimal pt every time
     */
    constructor(x: Int, y: Int, z: Int, w: Int) : this(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble()) {}

    companion object {
        fun point(x: Double, y: Double, z: Double) = Tuple(x, y, z, 1.0)
        fun point(x: Int, y: Int, z: Int) = Tuple(x, y, z, 1)
        fun vector(x: Double, y: Double, z: Double) = Tuple(x, y, z, 0.0)
        fun vector(x: Int, y: Int, z: Int) = Tuple(x, y, z, 0)
    }

    operator fun plus(other: Tuple) = zipWith(other, Double::plus)

    operator fun minus(other: Tuple) = zipWith(other, Double::minus)

    /**
     * Helper function that applies `f` to the elements. Useful for operator overloads
     */
    private fun mapTuple(f: (Double) -> Double): Tuple = Tuple(f(this.x), f(this.y), f(this.z), f(this.w))

    private fun zipWith(other: Tuple, f: (Double, Double) -> Double): Tuple =
        Tuple(
            f(this.x, other.x),
            f(this.y, other.y),
            f(this.z, other.z),
            f(this.w, other.w)
        )

    operator fun unaryMinus() = mapTuple { -it }

    operator fun times(scalar: Double) = mapTuple { it * scalar }

    operator fun div(scalar: Double) = mapTuple { it / scalar }

    fun magnitude(): Double {
        val lengthSquared = listOf(this.x, this.y, this.z, this.w)
            .sumOf { it * it }

        return sqrt(lengthSquared)
    }

    fun normalize(): Tuple {
        val magnitude = this.magnitude()
        return mapTuple { it / magnitude }
    }

    fun dot(other: Tuple): Double {
        return listOf(this.x, this.y, this.z, this.w)
            .zip(listOf(other.x, other.y, other.z, other.w))
            .sumOf { (a, b) -> a * b }
    }

    fun cross(other: Tuple) = vector(
        this.y * other.z - this.z * other.y,
        this.z * other.x - this.x * other.z,
        this.x * other.y - this.y * other.x
    )
}


