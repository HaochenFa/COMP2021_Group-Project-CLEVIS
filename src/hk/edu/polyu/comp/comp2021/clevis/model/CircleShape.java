package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Circle shape defined by its center and radius.
 */
public final class CircleShape extends AbstractShape {
    private double centerX;
    private double centerY;
    private final double radius;

    public CircleShape(String name, double centerX, double centerY, double radius) {
        super(name);
        if (radius <= 0) {
            throw new IllegalArgumentException("Circle must have positive radius");
        }
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.of(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    @Override
    public void move(double dx, double dy) {
        centerX += dx;
        centerY += dy;
    }

    @Override
    public boolean covers(Point2D point, double tolerance) {
        double dx = point.getX() - centerX;
        double dy = point.getY() - centerY;
        double distance = Math.hypot(dx, dy);
        return Math.abs(distance - radius) < tolerance;
    }

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
