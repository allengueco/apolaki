package base

import core.Matrix

interface Transformable<out T> {
    fun transform(transform: Matrix): T
}