package core

abstract class Shape : Normal, Intersect {
    var material = Material()
    var transform = Matrix.identity()

    abstract fun localIntersect(localRay: Ray): Intersections?
    abstract fun localNormal(localPoint: Point): Tuple

    /**
     * When intersecting the shape with a ray, all shapes need to
     * first convert the ray into object space, transforming ti by
     * the inverse of the shape's transformation matrix.
     */
    override fun intersect(ray: Ray): Intersections? {
        val localRay = ray.transform(transform.inverse())
        return localIntersect(localRay)
    }

    /**
     * When computing the normal vector, all shapes need to first convert the
     * point to object space, multiplying it by the inverse of the shape’s transformation matrix. Then, after
     * computing the normal they must transform it
     * by the inverse of the transpose of the transformation matrix, and then
     * normalize the resulting vector before returning it.
     */
    override fun normal(worldPoint: Tuple): Tuple {
        val localPoint = transform.inverse() * worldPoint
        val localNormal = localNormal(localPoint)
        val worldNormal = transform.inverse().transpose() * localNormal
        worldNormal.w = 0.0 // hacky way to preserve the normal being a vector
        return worldNormal.normalize()
    }
}
