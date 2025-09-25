package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupShapeTest {

    @Test
    void constructorRequiresMembers() {
        assertThrows(IllegalArgumentException.class, () -> new GroupShape("g", List.of()));
    }

    @Test
    void aggregatesBoundingBoxes() {
        RectangleShape rect = new RectangleShape("r", 0, 0, 2, 2);
        CircleShape circle = new CircleShape("c", 5, 5, 1);
        GroupShape group = new GroupShape("g", Arrays.asList(rect, circle));
        BoundingBox box = group.getBoundingBox();
        assertEquals(0, box.getMinX());
        assertEquals(0, box.getMinY());
        assertEquals(6, box.getMaxX());
        assertEquals(6, box.getMaxY());
    }

    @Test
    void movePropagatesToChildren() {
        RectangleShape rect = new RectangleShape("r", 0, 0, 2, 2);
        GroupShape group = new GroupShape("g", List.of(rect));
        group.move(1, 1);
        BoundingBox box = rect.getBoundingBox();
        assertEquals(1, box.getMinX());
        assertEquals(1, box.getMinY());
    }

    @Test
    void coversMatchesChildrenCoverage() {
        LineSegmentShape line = new LineSegmentShape("line", 0, 0, 2, 0);
        GroupShape group = new GroupShape("g", List.of(line));
        assertTrue(group.covers(new Point2D(1, 0.02), 0.05));
        assertFalse(group.covers(new Point2D(1, 0.2), 0.05));
    }

    @Test
    void isGroupIsAlwaysTrue() {
        GroupShape group = new GroupShape("g", List.of(new RectangleShape("r", 0, 0, 1, 1)));
        assertTrue(group.isGroup());
    }

    @Test
    void childrenListIsUnmodifiable() {
        GroupShape group = new GroupShape("g", List.of(new RectangleShape("r", 0, 0, 1, 1)));
        assertThrows(UnsupportedOperationException.class, () -> group.getChildren().add(new RectangleShape("r2", 0, 0, 1, 1)));
    }
}
