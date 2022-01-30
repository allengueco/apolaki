package base

import scene.Intersections
import core.Ray

interface Intersect {
    fun intersect(ray: Ray): Intersections?
}