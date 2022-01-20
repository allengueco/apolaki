package core

interface Intersect {
    fun intersect(ray: Ray): Intersections?
}