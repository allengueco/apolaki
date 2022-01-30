package core

import base.Transformable

class Ray(val origin: Tuple, val dir: Tuple) : Transformable<Ray> {
    fun at(t: Number) = this.origin + this.dir * t.toDouble()
    override fun transform(transform: Matrix): Ray = Ray(transform * this.origin, transform * this.dir)
}
