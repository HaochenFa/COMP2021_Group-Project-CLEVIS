package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Line segment shape described by two end points.
 */
public final class LineSegmentShape extends AbstractShape {
    private Point2D start;
    private Point2D end;

    public LineSegmentShape(String name, double x1, double y1, double x2, double y2) {
        super(name);
        if (x1 == x2 && y1 == y2) {
            throw new IllegalArgumentException("Line segment endpoints must differ");
        }
        this.start = new Point2D(x1, y1);
        this.end = new Point2D(x2, y2);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.fromPoints(start, end);
    }

    @Override
    public void move(double dx, double dy) {
        start = start.translate(dx, dy);
        end = end.translate(dx, dy);
    }

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

    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return end;
    }
}
