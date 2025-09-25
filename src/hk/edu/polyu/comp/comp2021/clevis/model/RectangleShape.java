/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Rectangle Shape using axis-aligned bounding logic.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Axis-aligned rectangle defined by its top-left corner.
 */
public class RectangleShape extends AbstractShape {
    private double x;
    private double y;
    private final double width;
    private final double height;

    /**
     * @constructor
     *
     * @param name   Shape identifier
     * @param x      Top-left corner `x`
     * @param y      Top-left corner `y`
     * @param width  Width of rectangle
     * @param height Height of rectangle
     */
    public RectangleShape(String name, double x, double y, double width, double height) {
        super(name);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Rectangle must have positive dimensions");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @function `getBoundingBox`
     *
     * @return Bounding box spanning the rectangle
     */
    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.of(x, y, x + width, y + height);
    }

    /**
     * @function `move` Translates the rectangle position.
     *
     * @param dx Delta along x-axis
     * @param dy Delta along y-axis
     */
    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**
     * @function `covers` Checks whether the point lies on the rectangle border.
     *
     * @param point     Candidate point
     * @param tolerance Distance tolerance for edge detection
     * @return `true` when the point is inside tolerance of any edge
     */
    @Override
    public boolean covers(Point2D point, double tolerance) {
        BoundingBox box = getBoundingBox();
        double px = point.getX();
        double py = point.getY();

        boolean insideX = px >= box.getMinX() - tolerance && px <= box.getMaxX() + tolerance;
        boolean insideY = py >= box.getMinY() - tolerance && py <= box.getMaxY() + tolerance;
        if (!insideX || !insideY) {
            return false;
        }

        boolean nearHorizontalEdge = (px >= box.getMinX() && px <= box.getMaxX()) &&
                (Math.abs(py - box.getMinY()) < tolerance || Math.abs(py - box.getMaxY()) < tolerance);
        boolean nearVerticalEdge = (py >= box.getMinY() && py <= box.getMaxY()) &&
                (Math.abs(px - box.getMinX()) < tolerance || Math.abs(px - box.getMaxX()) < tolerance);
        return nearHorizontalEdge || nearVerticalEdge;
    }

    /**
     * @function `getX`
     *
     * @return Top-left `x`
     */
    public double getX() {
        return x;
    }

    /**
     * @function `getY`
     *
     * @return Top-left `y`
     */
    public double getY() {
        return y;
    }

    /**
     * @function `getWidth`
     *
     * @return Rectangle width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @function `getHeight`
     *
     * @return Rectangle height
     */
    public double getHeight() {
        return height;
    }
}
