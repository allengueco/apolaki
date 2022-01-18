package core

sealed interface Object {
    fun intersect(ray: Ray): Intersections?
}