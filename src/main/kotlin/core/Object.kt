package core

sealed interface Object<T> {
    fun intersect(ray: Ray): Intersections<T>?
}