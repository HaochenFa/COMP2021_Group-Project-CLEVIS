package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Collections;
import java.util.List;

/**
 * Shared abstraction for all Clevis shapes.
 */
public interface Shape {
    String getName();

    BoundingBox getBoundingBox();

    void move(double dx, double dy);

    boolean covers(Point2D point, double tolerance);

    default boolean intersects(Shape other) {
        return getBoundingBox().intersects(other.getBoundingBox());
    }

    default boolean isGroup() {
        return false;
    }

    default List<Shape> getChildren() {
        return Collections.emptyList();
    }
}
