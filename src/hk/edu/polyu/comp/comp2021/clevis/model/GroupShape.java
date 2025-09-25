/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Group Shape for aggregating multiple shapes under a single name.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite shape orchestrating group-level operations across member shapes.
 */
public final class GroupShape extends AbstractShape {
    private final List<Shape> children;

    /**
     * @constructor Builds a new group with the given member shapes (non-empty).
     *
     * @param name    Group identifier
     * @param members Shapes to include within the group
     */
    public GroupShape(String name, List<Shape> members) {
        super(name);
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Group must contain at least one shape");
        }
        this.children = new ArrayList<>(members);
    }

    /**
     * @function `getBoundingBox` Aggregates the bbox of every child into one.
     *
     * @return Bounding box that tightly encloses all member shapes
     */
    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox aggregate = children.get(0).getBoundingBox();
        for (int i = 1; i < children.size(); i++) {
            aggregate = aggregate.expandToInclude(children.get(i).getBoundingBox());
        }
        return aggregate;
    }

    /**
     * @function `move` Applies the translation to each child.
     *
     * @param dx Delta movement in x-axis
     * @param dy Delta movement in y-axis
     */
    @Override
    public void move(double dx, double dy) {
        for (Shape child : children) {
            child.move(dx, dy);
        }
    }

    /**
     * @function `covers` Checks whether any child covers the provided point.
     *
     * @param point     Candidate point
     * @param tolerance Tolerance for edge inclusion checks
     * @return `true` if any member covers the point, `false` otherwise
     */
    @Override
    public boolean covers(Point2D point, double tolerance) {
        for (Shape child : children) {
            if (child.covers(point, tolerance)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @function `intersects` Delegates intersection check to each child.
     *
     * @param other Other shape to test against
     * @return `true` if any member intersects with the other shape
     */
    @Override
    public boolean intersects(Shape other) {
        for (Shape child : children) {
            if (child.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @function `isGroup`
     *
     * @return Always `true` for a group shape
     */
    @Override
    public boolean isGroup() {
        return true;
    }

    /**
     * @function `getChildren` Exposes an immutable view of group members.
     *
     * @return Unmodifiable list of child shapes
     */
    @Override
    public List<Shape> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
