/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Line Segment Shape defined by two endpoints.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Line segment shape described by two end points.
 */
public final class LineSegmentShape extends AbstractShape {
    private Point2D start;
    private Point2D end;

    /**
     * @constructor
     *
     * @param name Shape identifier
     * @param x1   Starting point `x`
     * @param y1   Starting point `y`
     * @param x2   Ending point `x`
     * @param y2   Ending point `y`
     */
    public LineSegmentShape(String name, double x1, double y1, double x2, double y2) {
        super(name);
        if (x1 == x2 && y1 == y2) {
            throw new IllegalArgumentException("Line segment endpoints must differ");
        }
        this.start = new Point2D(x1, y1);
        this.end = new Point2D(x2, y2);
    }

    /**
     * @function `getBoundingBox`
     *
     * @return Minimal bbox covering both endpoints
     */
    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.fromPoints(start, end);
    }

    /**
     * @function `move` Translates both endpoints.
     *
     * @param dx Delta along x-axis
     * @param dy Delta along y-axis
     */
    @Override
    public void move(double dx, double dy) {
        start = start.translate(dx, dy);
        end = end.translate(dx, dy);
    }

    /**
     * @function `covers` Determines whether the point lies near the segment.
     *
     * @param point     Candidate point
     * @param tolerance Distance threshold to consider covered
     * @return `true` when the point is within tolerance of the segment
     */
    @Override
    public boolean covers(Point2D point, double tolerance) {
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();
        double px = point.getX();
        double py = point.getY();

        double vx = x2 - x1;
        double vy = y2 - y1;
        double lengthSquared = vx * vx + vy * vy;
        double t = ((px - x1) * vx + (py - y1) * vy) / lengthSquared;
        t = Math.max(0.0, Math.min(1.0, t));
        double closestX = x1 + t * vx;
        double closestY = y1 + t * vy;
        double distance = Math.hypot(px - closestX, py - closestY);
        return distance < tolerance;
    }

    /**
     * @function `getStart`
     *
     * @return Starting endpoint
     */
    public Point2D getStart() {
        return start;
    }

    /**
     * @function `getEnd`
     *
     * @return Ending endpoint
     */
    public Point2D getEnd() {
        return end;
    }
}
