package core

import kotlin.math.abs
import kotlin.math.sqrt

data class Tuple(
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double
) {
    val red get() = this.x
    val green get() = this.y
    val blue get() = this.z
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

        fun color(x: Double, y: Double, z: Double) = vector(x, y, z) // default to the w component of color as 1

        fun color(x: Int, y: Int, z: Int) = vector(x, y, z)

    }


    /**
     * Helper function that applies `f` to the elements. Useful for operator overloads
     */
    private fun mapTuple(f: (Double) -> Double): Tuple = Tuple(f(this.x), f(this.y), f(this.z), f(this.w))

    /**
     * Helper function that applies an element-wise function `f` of `this` and `other`
     */
    private fun zipWith(other: Tuple, f: (Double, Double) -> Double): Tuple =
        Tuple(
            f(this.x, other.x),
            f(this.y, other.y),
            f(this.z, other.z),
            f(this.w, other.w)
        )

    operator fun unaryMinus() = mapTuple { -it }

    operator fun plus(other: Tuple) = zipWith(other, Double::plus)

    operator fun minus(other: Tuple) = zipWith(other, Double::minus)

    operator fun times(scalar: Double) = mapTuple { it * scalar }

    operator fun times(other: Tuple) = zipWith(other, Double::times)

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

    override fun equals(other: Any?): Boolean {
        val epsilon = .0000001
        fun equal(a: Double, b: Double) = abs(a - b) < epsilon
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tuple

        if (!equal(x, other.x)) return false
        if (!equal(y, other.y)) return false
        if (!equal(z, other.z)) return false
        if (!equal(w, other.w)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        return result
    }
}


