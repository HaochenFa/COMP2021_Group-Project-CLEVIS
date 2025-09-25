/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Square Shape inheriting rectangle behavior with equal sides.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Square specialization of a rectangle.
 */
public final class SquareShape extends RectangleShape {
    /**
     * @constructor
     *
     * @param name       Shape identifier
     * @param x          Top-left corner `x`
     * @param y          Top-left corner `y`
     * @param sideLength Side length of the square
     */
    public SquareShape(String name, double x, double y, double sideLength) {
        super(name, x, y, sideLength, sideLength);
        if (sideLength <= 0) {
            throw new IllegalArgumentException("Square must have positive side length");
        }
    }
}
