package core


class Matrix(init: () -> List<List<Double>>) {
    class Row(val idx: Int)
    class Col(val idx: Int)
    class Submatrix(val row: Int, val col: Int)
    class Minor(val row: Int, val col: Int)
    class Cofactor(val row: Int, val col: Int)


    val elements = init()
    val numRows get() = elements[0].size
    val numCols get() = elements.size
    val invertible get() = determinant != 0.0
    val determinant: Double by lazy { determinant() }

    operator fun get(index: Row): List<Double> {
        return elements[index.idx]
    }

    operator fun get(index: Col): List<Double> {
        return elements.map { elems -> elems[index.idx] }
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

    operator fun set(x: Int, y: Int, new: Double) {
        this[x, y] = new
    }

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

    fun transpose(): Matrix {
        return Matrix {
            List(elements.size) { idx -> this[Col(idx)] }
        }
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
                    this[Cofactor(c, r)] / determinant
                }
            }
        }
    }


    companion object {
        fun identity(numRows: Int = 4, numCols: Int = 4) = Matrix {
            List(numRows) { r ->
                List(numCols) { c ->
                    if (r == c) 1.0 else 0.0
                }
            }
        }
    }
}