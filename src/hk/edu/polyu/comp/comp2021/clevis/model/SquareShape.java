package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Square specialization of a rectangle.
 */
public final class SquareShape extends RectangleShape {
    public SquareShape(String name, double x, double y, double sideLength) {
        super(name, x, y, sideLength, sideLength);
        if (sideLength <= 0) {
            throw new IllegalArgumentException("Square must have positive side length");
        }
    }
}
