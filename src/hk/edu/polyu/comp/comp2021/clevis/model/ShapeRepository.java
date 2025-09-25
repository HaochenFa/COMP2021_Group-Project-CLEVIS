/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Repository Managing shape instances, grouping, and z-order.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Stores shapes with Z-order tracking and grouping metadata.
 */
public final class ShapeRepository {
    private final Map<String, ShapeEntry> shapes = new LinkedHashMap<>();
    private final List<Shape> zOrder = new ArrayList<>();

    /**
     * @function `contains`
     *
     * @param name Shape identifier
     * @return `true` when the repository already has a shape with the name
     */
    public boolean contains(String name) {
        return shapes.containsKey(name);
    }

    /**
     * @function `registerShape` Registers a new top-level shape.
     *
     * @param shape Shape instance to add
     */
    public void registerShape(Shape shape) {
        if (contains(shape.getName())) {
            throw new IllegalArgumentException("Shape name already exists: " + shape.getName());
        }
        ShapeEntry entry = new ShapeEntry(shape);
        shapes.put(shape.getName(), entry);
        zOrder.add(shape);
    }

    /**
     * @function `requireShape`
     *
     * @param name Shape identifier
     * @return Shape with the given name
     */
    public Shape requireShape(String name) {
        ShapeEntry entry = shapes.get(name);
        if (entry == null) {
            throw new IllegalArgumentException("Undefined shape: " + name);
        }
        return entry.shape;
    }

    /**
     * @function `requireTopLevelShape`
     *
     * @param name Shape identifier
     * @return Shape when it is not grouped; throws otherwise
     */
    public Shape requireTopLevelShape(String name) {
        ShapeEntry entry = requireEntry(name);
        if (entry.parent != null) {
            throw new IllegalStateException("Shape is currently grouped: " + name);
        }
        return entry.shape;
    }

    /**
     * @function `requireGroup`
     *
     * @param name Shape identifier
     * @return GroupShape instance matching the name
     */
    public GroupShape requireGroup(String name) {
        Shape shape = requireShape(name);
        if (!shape.isGroup()) {
            throw new IllegalArgumentException("Shape is not a group: " + name);
        }
        return (GroupShape) shape;
    }

    /**
     * @function `deleteShape` Removes a top-level shape (with recursion for
     *           groups).
     *
     * @param name Shape identifier
     */
    public void deleteShape(String name) {
        ShapeEntry entry = requireEntry(name);
        if (entry.parent != null) {
            throw new IllegalStateException("Cannot delete a shape that belongs to a group: " + name);
        }
        deleteRecursive(entry);
    }

    /**
     * @function `groupShapes` Creates a new group and assigns the provided members.
     *
     * @param groupName   Name of the new group
     * @param memberNames Shape names to include in the group
     * @return Newly created group
     */
    public GroupShape groupShapes(String groupName, List<String> memberNames) {
        if (memberNames == null || memberNames.isEmpty()) {
            throw new IllegalArgumentException("Group must contain at least one member");
        }
        if (contains(groupName)) {
            throw new IllegalArgumentException("Shape name already exists: " + groupName);
        }
        List<Shape> members = new ArrayList<>();
        for (String memberName : memberNames) {
            ShapeEntry entry = requireEntry(memberName);
            if (entry.parent != null) {
                throw new IllegalStateException("Shape already belongs to a group: " + memberName);
            }
            members.add(entry.shape);
        }

        GroupShape group = new GroupShape(groupName, members);
        registerShape(group);
        for (Shape member : members) {
            ShapeEntry entry = shapes.get(member.getName());
            entry.parent = group;
            zOrder.remove(entry.shape);
        }
        // Ensure the group sits on top after removing members.
        zOrder.remove(group);
        zOrder.add(group);
        return group;
    }

    /**
     * @function `ungroup` Restores individual members from a group.
     *
     * @param groupName Existing group name
     * @return Unmodifiable list of shapes restored to top-level
     */
    public List<Shape> ungroup(String groupName) {
        GroupShape group = requireGroup(groupName);
        ShapeEntry groupEntry = requireEntry(groupName);
        if (groupEntry.parent != null) {
            throw new IllegalStateException("Cannot ungroup nested group from outside");
        }
        int index = zOrder.indexOf(group);
        if (index < 0) {
            index = zOrder.size();
        }
        zOrder.remove(group);
        shapes.remove(groupName);

        List<Shape> restored = new ArrayList<>();
        List<Shape> children = group.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Shape child = children.get(i);
            ShapeEntry entry = requireEntry(child.getName());
            entry.parent = null;
            zOrder.add(index + i, child);
            restored.add(child);
        }
        return Collections.unmodifiableList(restored);
    }

    /**
     * @function `findTopShapeAt` Finds the top-most shape covering a point.
     *
     * @param point     Candidate point
     * @param tolerance Edge tolerance
     * @return Optional containing the top-most covering shape
     */
    public Optional<Shape> findTopShapeAt(Point2D point, double tolerance) {
        for (int i = zOrder.size() - 1; i >= 0; i--) {
            Shape shape = zOrder.get(i);
            if (shape.covers(point, tolerance)) {
                return Optional.of(shape);
            }
        }
        return Optional.empty();
    }

    /**
     * @function `listTopLevelShapes`
     *
     * @return Unmodifiable snapshot of the current top-level z-order
     */
    public List<Shape> listTopLevelShapes() {
        return Collections.unmodifiableList(new ArrayList<>(zOrder));
    }

    /**
     * @function `isTopLevel`
     *
     * @param name Shape identifier
     * @return `true` when the shape has no parent group
     */
    public boolean isTopLevel(String name) {
        ShapeEntry entry = shapes.get(name);
        return entry != null && entry.parent == null;
    }

    private void deleteRecursive(ShapeEntry entry) {
        if (entry.shape.isGroup()) {
            GroupShape group = (GroupShape) entry.shape;
            for (Shape child : group.getChildren()) {
                ShapeEntry childEntry = requireEntry(child.getName());
                deleteRecursive(childEntry);
            }
        }
        zOrder.remove(entry.shape);
        shapes.remove(entry.shape.getName());
    }

    private ShapeEntry requireEntry(String name) {
        ShapeEntry entry = shapes.get(name);
        if (entry == null) {
            throw new IllegalArgumentException("Undefined shape: " + name);
        }
        return entry;
    }

    /**
     * Storage wrapper capturing per-shape metadata.
     */
    private static final class ShapeEntry {
        private final Shape shape;
        private GroupShape parent;

        private ShapeEntry(Shape shape) {
            this.shape = shape;
        }
    }
}
