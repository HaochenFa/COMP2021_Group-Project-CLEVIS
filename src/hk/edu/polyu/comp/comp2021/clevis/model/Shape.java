/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Shape Interface defining common behavior for all Clevis shapes.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Collections;
import java.util.List;

/**
 * Shared abstraction for all Clevis shapes.
 */
public interface Shape {
    /**
     * @function `getName`
     *
     * @return Unique identifier of this shape
     */
    String getName();

    /**
     * @function `getBoundingBox`
     *
     * @return Axis-aligned bounding box that encloses the shape
     */
    BoundingBox getBoundingBox();

    /**
     * @function `move` Translates the shape by the provided delta.
     *
     * @param dx Movement along x-axis
     * @param dy Movement along y-axis
     */
    void move(double dx, double dy);

    /**
     * @function `covers` Checks whether the shape touches the given point.
     *
     * @param point     Candidate point
     * @param tolerance Tolerance used for edge proximity
     * @return `true` when the point lies on or near the shape
     */
    boolean covers(Point2D point, double tolerance);

    /**
     * @function `intersects` Default AABB overlap test between shapes.
     *
     * @param other Shape to compare against
     * @return `true` when bounding boxes overlap
     */
    default boolean intersects(Shape other) {
        return getBoundingBox().intersects(other.getBoundingBox());
    }

    /**
     * @function `isGroup`
     *
     * @return `true` when shape represents a group; `false` otherwise
     */
    default boolean isGroup() {
        return false;
    }

    /**
     * @function `getChildren` Supplies members for composite shapes.
     *
     * @return Immutable list of child shapes (empty by default)
     */
    default List<Shape> getChildren() {
        return Collections.emptyList();
    }
}
