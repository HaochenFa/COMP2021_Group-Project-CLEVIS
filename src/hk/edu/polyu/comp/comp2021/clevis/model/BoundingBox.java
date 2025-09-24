package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Axis-aligned bounding box helper used for intersection and listing logic.
 */
public final class BoundingBox {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    private BoundingBox(double minX, double minY, double maxX, double maxY) {
        if (maxX < minX || maxY < minY) {
            throw new IllegalArgumentException("Invalid bounds");
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public static BoundingBox of(double minX, double minY, double maxX, double maxY) {
        return new BoundingBox(minX, minY, maxX, maxY);
    }

    public static BoundingBox fromPoints(Point2D p1, Point2D p2) {
        double minX = Math.min(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());
        double maxX = Math.max(p1.getX(), p2.getX());
        double maxY = Math.max(p1.getY(), p2.getY());
        return new BoundingBox(minX, minY, maxX, maxY);
    }

    public BoundingBox expandToInclude(BoundingBox other) {
        return new BoundingBox(
                Math.min(minX, other.minX),
                Math.min(minY, other.minY),
                Math.max(maxX, other.maxX),
                Math.max(maxY, other.maxY));
    }

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

    public boolean intersects(BoundingBox other) {
        return maxX > other.minX && other.maxX > minX && maxY > other.minY && other.maxY > minY;
    }

    @Override
    public String toString() {
        return getMinX() + " " + getMinY() + " " + getWidth() + " " + getHeight();
    }
}
