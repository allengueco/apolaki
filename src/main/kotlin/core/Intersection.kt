package core

class Intersection<T>(val t: Number, val obj: T)

/**
 * TODO: This might be a problem when the intersections are all different objects. Maybe introduce generics?
 */
typealias Intersections<T> = List<Intersection<T>>

fun <T> Intersections<T>.count() = this.size

