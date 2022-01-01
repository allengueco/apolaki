import kotlin.math.abs

class Utils {
    companion object {
        fun equals(d1: Double, d2: Double): Boolean {
            val epsilon = .0001
            return abs(d1 - d2) < epsilon
        }
    }
}