package core

sealed interface Transformable<out T> {
    fun transform(transform: Matrix): T
}