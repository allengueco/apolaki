import kotlin.math.abs

class Utils {
    companion object {
        const val EPSILON: Double = 0.0001
        /**
         * Need this to mitigate floating point errors
         */
        fun equals(d1: Double, d2: Double): Boolean {
            return abs(d1 - d2) < EPSILON
        }
    }
}