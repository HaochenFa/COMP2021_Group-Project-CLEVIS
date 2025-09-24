package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Immutable point in the 2D plane.
 */
public final class Point2D {
    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point2D translate(double dx, double dy) {
        return new Point2D(x + dx, y + dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
