/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @Description Circle Class.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Circle shape defined by its center and radius.
 */
public final class CircleShape extends AbstractShape {
    private double centerX;
    private double centerY;
    private final double radius;

    /**
     * @constructor
     * 
     * @param name
     * @param centerX Center `x` coordinate
     * @param centerY Center `y` coordinate
     * @param radius
     */
    public CircleShape(String name, double centerX, double centerY, double radius) {
        super(name);
        if (radius <= 0) {
            throw new IllegalArgumentException("Circle must have positive radius");
        }
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    /**
     * @function `getBoundingBox`
     * 
     * @return A BBox that encloses the circle
     */
    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.of(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    @Override
    public void move(double dx, double dy) {
        centerX += dx;
        centerY += dy;
    }

    /**
     * @function `covers` Checks whether a pint lies on the circumference w/n a
     *           tolerance (epsilon) by computing the Euc. distance from the center
     *           and comparing it to radius
     * 
     * @param point   The point candidate
     * @param epsilon The tolerance value
     * @return
     */
    @Override
    public boolean covers(Point2D point, double epsilon) {
        double dx = point.getX() - centerX;
        double dy = point.getY() - centerY;
        double distance = Math.hypot(dx, dy);
        return Math.abs(distance - radius) < epsilon;
    }

    // @accessors
    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }
}
