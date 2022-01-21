package core

import Utils
import kotlin.math.cos
import kotlin.math.sin


class Matrix(init: () -> List<List<Double>>) {
    class Row(val row: Int)
    class Col(val col: Int)
    class Submatrix(val row: Int, val col: Int)
    class Minor(val row: Int, val col: Int)
    class Cofactor(val row: Int, val col: Int)


    private val elements by lazy { init() }
    private val numRows get() = elements[0].size
    private val numCols get() = elements.size
    val invertible: Boolean by lazy { determinant != 0.0 }
    val determinant: Double by lazy { determinant() }

    operator fun get(index: Row): List<Double> {
        return elements[index.row]
    }

    operator fun get(index: Col): List<Double> {
        return elements.map { elems -> elems[index.col] }
    }

    operator fun get(index: Submatrix): Matrix {
        return Matrix {
            elements
                .filterIndexed { rowIdx, _ -> rowIdx != index.row }
                .map { row -> row.filterIndexed { colIdx, _ -> colIdx != index.col } }
        }
    }

    operator fun get(index: Minor): Double {
        return this[Submatrix(index.row, index.col)].determinant()
    }

    operator fun get(index: Cofactor): Double {
        val minor = this[Minor(index.row, index.col)]
        return if ((index.row + index.col) % 2 == 0) minor else -minor
    }

    operator fun get(x: Int, y: Int) = elements[x][y]

//    operator fun set(x: Int, y: Int, new: Double) {
//        elements[x][y] = new
//    }

    /**
     * Matrix * Matrix = Matrix
     */
    operator fun times(other: Matrix): Matrix {
        return Matrix {
            List(numRows) { r ->
                List(numCols) { c ->
                    this[Row(r)].zip(other[Col(c)]).sumOf { it.first * it.second }
                }
            }
        }
    }

    /**
     * Matrix * Tuple = Tuple
     */
    operator fun times(other: Tuple): Tuple {
        return Tuple(
            this[Row(0)].zip(other).sumOf { it.first * it.second },
            this[Row(1)].zip(other).sumOf { it.first * it.second },
            this[Row(2)].zip(other).sumOf { it.first * it.second },
            this[Row(3)].zip(other).sumOf { it.first * it.second }
        )
    }


    override fun toString(): String {
        val formatTemplate = "% 9.3f "
        return elements
            .joinToString("\n") { row ->
                row.joinToString("|", "|", "|") { String.format(formatTemplate, it) }
            }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix
        val elementsAreEqual = elements.flatten().zip(other.elements.flatten())
            .all { Utils.equals(it.first, it.second) }
        if (!elementsAreEqual) return false

        return true
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    private fun determinant(): Double {
        return when {
            numRows == 2 && numRows == 2 -> {
                this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0]
            }
            else -> {
                (0 until numCols)
                    .fold(0.0) { det, column -> det + this[0, column] * this[Cofactor(0, column)] }
            }
        }
    }

    fun inverse(): Matrix {
        if (!this.invertible) throw Exception("Matrix is not invertible")
        return Matrix {
            List(numRows) { r ->
                List(numCols) { c ->
                    this[Cofactor(c, r)] / determinant // (c, r) because it needs to be transposed
                }
            }
        }
    }

    fun transpose(): Matrix {
        return Matrix {
            List(elements.size) { idx -> this[Col(idx)] }
        }
    }

    fun translate(x: Number, y: Number, z: Number) =
        translation(x.toDouble(), y.toDouble(), z.toDouble()) * this

    fun scale(x: Number, y: Number, z: Number) =
        scaling(x.toDouble(), y.toDouble(), z.toDouble()) * this

    fun rotateX(radians: Number) = rotationX(radians.toDouble()) * this

    fun rotateY(radians: Number) = rotationY(radians.toDouble()) * this

    fun rotateZ(radians: Number) = rotationZ(radians.toDouble()) * this

    fun shear(x: Pair<Number, Number>, y: Pair<Number, Number>, z: Pair<Number, Number>) =
        shearing(x, y, z) * this

    fun pipe(vararg transform: Matrix) = transform.fold(this) { acc, tr -> tr * acc }

    companion object {
        private fun identityList(numRows: Int = 4, numCols: Int = 4) =
            MutableList(numRows) { r ->
                MutableList(numCols) { c ->
                    if (r == c) 1.0 else 0.0
                }
            }

        fun identity(numRows: Int = 4, numCols: Int = 4) = Matrix {
            identityList(numRows, numCols).map {
                it.toMutableList()
            }.toMutableList()
        }

        fun translation(x: Double, y: Double, z: Double): Matrix {
            val identity = identityList()
            identity[0][3] = x
            identity[1][3] = y
            identity[2][3] = z

            return identity.toMatrix()
        }

        fun translation(x: Int, y: Int, z: Int) =
            translation(x.toDouble(), y.toDouble(), z.toDouble())

        fun scaling(x: Int, y: Int, z: Int) =
            scaling(x.toDouble(), y.toDouble(), z.toDouble())

        fun scaling(x: Double, y: Double, z: Double): Matrix {
            val identity = identityList()
            identity[0][0] = x
            identity[1][1] = y
            identity[2][2] = z

            return identity.toMatrix()
        }

        fun rotationX(radians: Double): Matrix {
            val identity = identityList()
            identity[1][1] = cos(radians)
            identity[1][2] = -sin(radians)
            identity[2][1] = sin(radians)
            identity[2][2] = cos(radians)

            return identity.toMatrix()
        }

        fun rotationY(radians: Double): Matrix {
            val identity = identityList()
            identity[0][0] = cos(radians)
            identity[0][2] = sin(radians)
            identity[2][0] = -sin(radians)
            identity[2][2] = cos(radians)

            return identity.toMatrix()
        }

        fun rotationZ(radians: Double): Matrix {
            val identity = identityList()
            identity[0][0] = cos(radians)
            identity[0][1] = -sin(radians)
            identity[1][0] = sin(radians)
            identity[1][1] = cos(radians)
            return identity.toMatrix()
        }

        fun shearing(x: Pair<Number, Number>, y: Pair<Number, Number>, z: Pair<Number, Number>): Matrix {
            val identity = identityList()
            return identity
                .apply {
                    this[0][1] = x.first.toDouble()
                    this[0][2] = x.second.toDouble()
                }
                .apply {
                    this[1][0] = y.first.toDouble()
                    this[1][2] = y.second.toDouble()
                }
                .apply {
                    this[2][0] = z.first.toDouble()
                    this[2][1] = z.second.toDouble()
                }
                .toMatrix()
        }

        fun view(from: Tuple, to: Tuple, up: Tuple): Matrix {
            val forward = (to - from).normalize()
            val left = forward.cross(up.normalize())
            val trueUp = left.cross(forward)

            return Matrix {
                listOf(
                    listOf(left.x, left.y, left.z, 0.0),
                    listOf(trueUp.x, trueUp.y, trueUp.z, 0.0),
                    listOf(-forward.x, -forward.y, -forward.z, 0.0),
                    listOf(0.0, 0.0, 0.0, 1.0)
                )
            } * translation(-from.x, -from.y, -from.z)
        }
    }
}
fun List<List<Double>>.toMatrix() = Matrix { this }