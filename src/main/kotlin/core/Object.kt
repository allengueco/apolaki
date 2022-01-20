package core

sealed interface Object {
    var material: Material
    fun normal(worldPoint: Tuple): Tuple
}