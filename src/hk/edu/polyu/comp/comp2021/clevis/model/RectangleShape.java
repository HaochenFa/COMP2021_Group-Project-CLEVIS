package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Axis-aligned rectangle defined by its top-left corner.
 */
public class RectangleShape extends AbstractShape {
    private double x;
    private double y;
    private final double width;
    private final double height;

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

    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.of(x, y, x + width, y + height);
    }

    @Override
    public void move(double dx, double dy) {
        x += dx;
        y += dy;
    }

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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
