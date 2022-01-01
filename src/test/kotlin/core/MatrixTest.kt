package core

import Utils.Companion.equals
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class MatrixTest {
    @Test
    fun `Constructing and inspecting a 4x4 matrix`() {
        val m = Matrix {
            listOf(
                listOf(1.0, 2.0, 3.0, 4.0),
                listOf(5.5, 6.5, 7.5, 8.5),
                listOf(9.0, 10.0, 11.0, 12.0),
                listOf(13.5, 14.5, 15.5, 16.5)
            )
        }
        assertAll(
            Executable { assertTrue { m[0, 0] == 1.0 } },
            Executable { assertTrue { m[0, 3] == 4.0 } },
            Executable { assertTrue { m[1, 0] == 5.5 } },
            Executable { assertTrue { m[1, 2] == 7.5 } },
            Executable { assertTrue { m[2, 2] == 11.0 } },
            Executable { assertTrue { m[3, 0] == 13.5 } },
            Executable { assertTrue { m[3, 2] == 15.5 } }
        )
    }

    @Test
    fun `A 2x2 matrix out to be representable`() {
        val m = Matrix {
            listOf(
                listOf(-3.0, 5.0),
                listOf(1.0, -2.0),
            )
        }
        assertAll(
            Executable { assertTrue { m[0, 0] == -3.0 } },
            Executable { assertTrue { m[0, 1] == 5.0 } },
            Executable { assertTrue { m[1, 0] == 1.0 } },
            Executable { assertTrue { m[1, 1] == -2.0 } },
        )
    }

    @Test
    fun `A 3x3 matrix out to be representable`() {
        val m = Matrix {
            listOf(
                listOf(-3.0, 5.0, 0.0),
                listOf(1.0, -2.0, 7.0),
                listOf(0.0, 1.0, 1.0)
            )
        }
        assertAll(
            Executable { assertTrue { m[0, 0] == -3.0 } },
            Executable { assertTrue { m[1, 1] == -2.0 } },
            Executable { assertTrue { m[2, 2] == 1.0 } },
        )
    }

    @Test
    fun `Matrix equality with identical matrices`() {
        val init = {
            listOf(
                listOf(1.0, 2.0, 3.0, 4.0),
                listOf(5.0, 6.0, 7.0, 8.0),
                listOf(9.0, 8.0, 7.0, 6.0),
                listOf(5.0, 4.0, 3.0, 2.0)
            )
        }

        val a = Matrix { init() }
        val b = Matrix { init() }

        assertTrue(a == b)
    }

    @Test
    fun `Matrix equality with different matrices`() {
        val a = Matrix {
            listOf(
                listOf(1.0, 2.0, 3.0, 4.0),
                listOf(5.0, 6.0, 7.0, 8.0),
                listOf(9.0, 8.0, 7.0, 6.0),
                listOf(5.0, 4.0, 3.0, 2.0)
            )
        }
        val b = Matrix {
            listOf(
                listOf(2.0, 3.0, 4.0, 5.0),
                listOf(6.0, 7.0, 8.0, 9.0),
                listOf(8.0, 7.0, 6.0, 5.0),
                listOf(4.0, 3.0, 2.0, 1.0)
            )
        }

        assertTrue(a != b)
    }

    @Test
    fun `Multiplying two matrices`() {
        val a = Matrix {
            listOf(
                listOf(1.0, 2.0, 3.0, 4.0),
                listOf(5.0, 6.0, 7.0, 8.0),
                listOf(9.0, 8.0, 7.0, 6.0),
                listOf(5.0, 4.0, 3.0, 2.0)
            )
        }
        val b = Matrix {
            listOf(
                listOf(-2.0, 1.0, 2.0, 3.0),
                listOf(3.0, 2.0, 1.0, -1.0),
                listOf(4.0, 3.0, 6.0, 5.0),
                listOf(1.0, 2.0, 7.0, 8.0)
            )
        }

        val expected = Matrix {
            listOf(
                listOf(20.0, 22.0, 50.0, 48.0),
                listOf(44.0, 54.0, 114.0, 108.0),
                listOf(40.0, 58.0, 110.0, 102.0),
                listOf(16.0, 26.0, 46.0, 42.0)
            )
        }

        assertEquals(expected, a * b)
    }

    @Test
    fun `A matrix multiplied by at tuple`() {
        val A = Matrix {
            listOf(
                listOf(1.0, 2.0, 3.0, 4.0),
                listOf(2.0, 4.0, 4.0, 2.0),
                listOf(8.0, 6.0, 4.0, 1.0),
                listOf(0.0, 0.0, 0.0, 1.0)
            )
        }
        val b = Tuple(1, 2, 3, 1)

        assertEquals(Tuple(18, 24, 33, 1), A * b)
    }

    @Test
    fun `Multiplying a matrix by the identity matrix AND tuple`() {
        val A = Matrix {
            listOf(
                listOf(0.0, 1.0, 2.0, 4.0),
                listOf(1.0, 2.0, 4.0, 8.0),
                listOf(2.0, 4.0, 8.0, 16.0),
                listOf(4.0, 8.0, 16.0, 32.0)
            )
        }
        val t = Tuple(1, 2, 3, 4)

        assertEquals(A, A * Matrix.identity())
        assertEquals(t, Matrix.identity() * t)
    }

    @Test
    fun `Transposing a matrix`() {
        val A = Matrix {
            listOf(
                listOf(0.0, 9.0, 3.0, 0.0),
                listOf(9.0, 8.0, 0.0, 8.0),
                listOf(1.0, 8.0, 5.0, 3.0),
                listOf(0.0, 0.0, 5.0, 8.0)
            )
        }
        val expected = Matrix {
            listOf(
                listOf(0.0, 9.0, 1.0, 0.0),
                listOf(9.0, 8.0, 8.0, 0.0),
                listOf(3.0, 0.0, 5.0, 5.0),
                listOf(0.0, 8.0, 3.0, 8.0),
            )
        }

        assertEquals(expected, A.transpose())
    }

    @Test
    fun `Transposing the identity matrix`() {
        assertEquals(Matrix.identity(), Matrix.identity().transpose())
    }

    @Test
    fun `Calculating the determinant of a 2x2 matrix`() {
        val A = Matrix {
            listOf(
                listOf(1.0, 5.0),
                listOf(-3.0, 2.0)
            )
        }

        assertEquals(17.0, A.determinant)
    }

    @Test
    fun `A submatrix of a 3x3 matrix is a 2x2 matrix`() {
        val A = Matrix {
            listOf(
                listOf(1.0, 5.0, 0.0),
                listOf(-3.0, 2.0, 7.0),
                listOf(0.0, 6.0, -3.0)
            )
        }

        val expected = Matrix {
            listOf(
                listOf(-3.0, 2.0),
                listOf(0.0, 6.0)
            )
        }

        assertEquals(expected, A[Matrix.Submatrix(0, 2)])
    }

    @Test
    fun `A submatrix of a 4x4 matrix is a 3x3 matrix`() {
        val A = Matrix {
            listOf(
                listOf(-6.0, 1.0, 1.0, 6.0),
                listOf(-8.0, 5.0, 8.0, 6.0),
                listOf(-1.0, 0.0, 8.0, 2.0),
                listOf(-7.0, 1.0, -1.0, 1.0)
            )
        }

        val expected = Matrix {
            listOf(
                listOf(-6.0, 1.0, 6.0),
                listOf(-8.0, 8.0, 6.0),
                listOf(-7.0, -1.0, 1.0)
            )
        }

        assertEquals(expected, A[Matrix.Submatrix(2, 1)])
    }

    @Test
    fun `Calculating a minor of a 3x3 matrix`() {
        val A = Matrix {
            listOf(
                listOf(3.0, 5.0, 0.0),
                listOf(2.0, -1.0, -7.0),
                listOf(6.0, -1.0, 5.0)
            )
        }

        val B = A[Matrix.Submatrix(1, 0)]

        assertAll(
            { assertEquals(25.0, B.determinant) },
            { assertEquals(25.0, A[Matrix.Minor(1, 0)]) }
        )
    }

    @Test
    fun `Calculating a cofactor of a 3x3 matrix`() {
        val A = Matrix {
            listOf(
                listOf(3.0, 5.0, 0.0),
                listOf(2.0, -1.0, -7.0),
                listOf(6.0, -1.0, 5.0)
            )
        }

        assertAll(
            { assertEquals(-12.0, A[Matrix.Minor(0, 0)]) },
            { assertEquals(-12.0, A[Matrix.Cofactor(0, 0)]) },
            { assertEquals(25.0, A[Matrix.Minor(1, 0)]) },
            { assertEquals(-25.0, A[Matrix.Cofactor(1, 0)]) },
        )
    }

    @Test
    fun `Calculating the determinant of a 3x3 matrix`() {
        val A = Matrix {
            listOf(
                listOf(1.0, 2.0, 6.0),
                listOf(-5.0, 8.0, -4.0),
                listOf(2.0, 6.0, 4.0)
            )
        }

        assertAll(
            { assertEquals(56.0, A[Matrix.Cofactor(0, 0)]) },
            { assertEquals(12.0, A[Matrix.Cofactor(0, 1)]) },
            { assertEquals(-46.0, A[Matrix.Cofactor(0, 2)]) },
            { assertEquals(-196.0, A.determinant) }
        )
    }

    @Test
    fun `Calculating the determinant of a 4x4 matrix`() {
        val A = Matrix {
            listOf(
                listOf(-2.0, -8.0, 3.0, 5.0),
                listOf(-3.0, 1.0, 7.0, 3.0),
                listOf(1.0, 2.0, -9.0, 6.0),
                listOf(-6.0, 7.0, 7.0, -9.0)
            )
        }

        assertAll(
            { assertEquals(690.0, A[Matrix.Cofactor(0, 0)]) },
            { assertEquals(447.0, A[Matrix.Cofactor(0, 1)]) },
            { assertEquals(210.0, A[Matrix.Cofactor(0, 2)]) },
            { assertEquals(51.0, A[Matrix.Cofactor(0, 3)]) },
            { assertEquals(-4071.0, A.determinant) }
        )
    }

    @Test
    fun `Testing an invertible matrix for invertibility`() {
        val A = Matrix {
            listOf(
                listOf(6.0, 4.0, 4.0, 4.0),
                listOf(5.0, 5.0, 7.0, 6.0),
                listOf(4.0, -9.0, 3.0, -7.0),
                listOf(9.0, 1.0, 7.0, -6.0)
            )
        }

        assertAll(
            { assertEquals(-2120.0, A.determinant) },
            { assertTrue(A.invertible) }
        )
    }

    @Test
    fun `Testing an noninvertible matrix for invertibility`() {
        val A = Matrix {
            listOf(
                listOf(-4.0, 2.0, -2.0, -3.0),
                listOf(9.0, 6.0, 2.0, 6.0),
                listOf(0.0, -5.0, 1.0, -5.0),
                listOf(0.0, 0.0, 0.0, 0.0)
            )
        }

        assertAll(
            { assertEquals(0.0, A.determinant) },
            { assertFalse(A.invertible) }
        )
    }

    @Test
    fun `Calculating the inverse of a matrix`() {
        val A = Matrix {
            listOf(
                listOf(-5.0, 2.0, 6.0, -8.0),
                listOf(1.0, -5.0, 1.0, 8.0),
                listOf(7.0, 7.0, -6.0, -7.0),
                listOf(1.0, -3.0, 7.0, 4.0)
            )
        }

        val B = A.inverse()
        val expected = Matrix {
            listOf(
                listOf(0.21805, 0.45113, 0.24060, -0.04511),
                listOf(-0.80827, -1.45677, -0.44361, 0.52068),
                listOf(-0.07895, -0.22368, -0.05263, 0.19737),
                listOf(-0.52256, -0.81391, -0.30075, 0.30639)
            )
        }

        assertAll(
            { assertEquals(532.0, A.determinant) },
            { assertEquals(-160.0, A[Matrix.Cofactor(2, 3)]) },
            { assertTrue(equals(-160.0 / 532.0, B[3, 2])) },
            { assertEquals(105.0, A[Matrix.Cofactor(3, 2)]) },
            { assertTrue(equals(105.0 / 532.0, B[2, 3])) },
            { assertEquals(expected, B) }
        )
    }

    @Test
    fun `Calculating the inverse of another matrix`() {
        val A = Matrix {
            listOf(
                listOf(8.0, -5.0, 9.0, 2.0),
                listOf(7.0, 5.0, 6.0, 1.0),
                listOf(-6.0, 0.0, 9.0, 6.0),
                listOf(-3.0, 0.0, -9.0, -4.0)
            )
        }
        val expected = Matrix {
            listOf(
                listOf(-0.15385, -0.15385, -0.28205, -0.53846),
                listOf(-0.07692, 0.12308, 0.02564, 0.03077),
                listOf(0.35897, 0.35897, 0.43590, 0.92308),
                listOf(-0.69231, -0.69231, -0.76923, -1.92308)
            )
        }

        assertEquals(expected, A.inverse())
    }

    @Test
    fun `Calculating the inverse of third matrix`() {
        val A = Matrix {
            listOf(
                listOf(9.0, 3.0, 0.0, 9.0),
                listOf(-5.0, -2.0, -6.0, -3.0),
                listOf(-4.0, 9.0, 6.0, 4.0),
                listOf(-7.0, 6.0, 6.0, 2.0)
            )
        }
        val expected = Matrix {
            listOf(
                listOf(-0.04074, -0.07778, 0.14444, -0.22222),
                listOf(-0.07778, 0.03333, 0.36667, -0.33333),
                listOf(-0.02901, -0.14630, -0.10926, 0.12963),
                listOf(0.17778, 0.06667, -0.26667, 0.33333)
            )
        }

        assertEquals(expected, A.inverse())
    }

    @Test
    fun `Multiplying a product by its inverse`() {
        val A = Matrix {
            listOf(
                listOf(3.0, -9.0, 7.0, 3.0),
                listOf(3.0, -8.0, 2.0, -9.0),
                listOf(-4.0, 4.0, 4.0, 1.0),
                listOf(-6.0, 5.0, -1.0, 1.0)
            )
        }

        val B = Matrix {
            listOf(
                listOf(8.0, 2.0, 2.0, 2.0),
                listOf(3.0, -1.0, 7.0, 0.0),
                listOf(7.0, 0.0, 5.0, 4.0),
                listOf(6.0, -2.0, 0.0, 5.0)
            )
        }

        val C = A * B

        assertEquals(A, C * B.inverse())
    }
}