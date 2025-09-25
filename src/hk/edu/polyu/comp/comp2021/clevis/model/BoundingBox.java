/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Bounding Box (bbox) Helper, Stores Min/Max Coordinates for Axis-Aligned Rectangle.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Axis-aligned bounding box helper used for intersection and listing logic.
 */
public final class BoundingBox {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    /**
     * @constructor use `private` to enforce Validation & Immutability.
     * 
     * @param minX Minimum `x` coordinate
     * @param minY Minimum `y` coordinate
     * @param maxX Maximum `x` coordinate
     * @param maxY Maximum `y` coordinate
     */
    private BoundingBox(double minX, double minY, double maxX, double maxY) {
        if (maxX < minX || maxY < minY) {
            throw new IllegalArgumentException("Invalid bounds");
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * @function `of` Build new Instance from RAW coordinates.
     * 
     * @param minX Minimum `x` coordinate
     * @param minY Minimum `y` coordinate
     * @param maxX Maximum `x` coordinate
     * @param maxY Maximum `y` coordinate
     * @return A new BBox Instance
     */
    public static BoundingBox of(double minX, double minY, double maxX, double maxY) {
        return new BoundingBox(minX, minY, maxX, maxY);
    }

    /**
     * @function `fromPoints` Build new Instance from `Point2D` endpoints.
     * 
     * @param p1 `Point2D` endpoints
     * @param p2 `Point2D` endpoints
     * @return A new BBox Instance
     */
    public static BoundingBox fromPoints(Point2D p1, Point2D p2) {
        // We don't assume the args are the true bottom-left/top-right coord
        double minX = Math.min(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());
        double maxX = Math.max(p1.getX(), p2.getX());
        double maxY = Math.max(p1.getY(), p2.getY());

        return new BoundingBox(minX, minY, maxX, maxY);
    }

    /**
     * @function `expandToInclude` Minimally Encloses the current BBox & another,
     *           aggregating w/o mutation.
     * 
     * @param other Another BBox
     * @return A new BBox that includes the original one and another
     */
    public BoundingBox expandToInclude(BoundingBox other) {
        return new BoundingBox(
                Math.min(minX, other.minX),
                Math.min(minY, other.minY),
                Math.max(maxX, other.maxX),
                Math.max(maxY, other.maxY));
    }

    // @accessors
    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getWidth() {
        return maxX - minX;
    }

    public double getHeight() {
        return maxY - minY;
    }

    /**
     * @function `intersects` Performs an AABB overlap test.
     * 
     * @param other Other BBox
     * @return `true` when the two BBox overlaps, `false` when they're separate or
     *         touch along edges/corners
     */
    public boolean intersects(BoundingBox other) {
        return maxX > other.minX && other.maxX > minX && maxY > other.minY && other.maxY > minY;
    }

    @Override
    public String toString() {
        return getMinX() + " " + getMinY() + " " + getWidth() + " " + getHeight();
    }
}
