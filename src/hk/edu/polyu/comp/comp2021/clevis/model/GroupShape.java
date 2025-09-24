package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite shape aggregating other shapes.
 */
public final class GroupShape extends AbstractShape {
    private final List<Shape> children;

    public GroupShape(String name, List<Shape> members) {
        super(name);
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Group must contain at least one shape");
        }
        this.children = new ArrayList<Shape>(members);
    }

    @Override
    public BoundingBox getBoundingBox() {
        BoundingBox aggregate = children.get(0).getBoundingBox();
        for (int i = 1; i < children.size(); i++) {
            aggregate = aggregate.expandToInclude(children.get(i).getBoundingBox());
        }
        return aggregate;
    }

    @Override
    public void move(double dx, double dy) {
        for (Shape child : children) {
            child.move(dx, dy);
        }
    }

    @Override
    public boolean covers(Point2D point, double tolerance) {
        for (Shape child : children) {
            if (child.covers(point, tolerance)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersects(Shape other) {
        for (Shape child : children) {
            if (child.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public List<Shape> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
