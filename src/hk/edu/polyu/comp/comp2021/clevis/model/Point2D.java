/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Point Implementation storing immutable 2D coordinates.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Immutable point in the 2D plane.
 */
public final class Point2D {
    private final double x;
    private final double y;

    /**
     * @constructor
     *
     * @param x Horizontal coordinate
     * @param y Vertical coordinate
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @function `getX`
     *
     * @return `x` coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * @function `getY`
     *
     * @return `y` coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * @function `translate` Returns a new point translated by the given delta.
     *
     * @param dx Delta along x-axis
     * @param dy Delta along y-axis
     * @return Newly translated point instance
     */
    public Point2D translate(double dx, double dy) {
        return new Point2D(x + dx, y + dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
