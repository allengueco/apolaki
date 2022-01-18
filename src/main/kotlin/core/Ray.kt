package core

class Ray(val origin: Tuple, val dir: Tuple) {
    fun at(t: Number) = this.origin + this.dir * t.toDouble()
}
