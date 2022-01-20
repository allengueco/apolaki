package core

sealed interface Object {
    var material: Material
    fun intersect(ray: Ray): Intersections?
    fun normal(worldPoint: Tuple): Tuple
}