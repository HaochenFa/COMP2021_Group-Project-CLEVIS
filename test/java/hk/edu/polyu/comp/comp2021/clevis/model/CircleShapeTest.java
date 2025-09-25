package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleShapeTest {

    @Test
    void constructorRequiresPositiveRadius() {
        assertThrows(IllegalArgumentException.class, () -> new CircleShape("c", 0, 0, 0));
    }

    @Test
    void boundingBoxExpandsFromCenter() {
        CircleShape circle = new CircleShape("c", 2, -1, 3);
        BoundingBox box = circle.getBoundingBox();
        assertEquals(-1, box.getMinX());
        assertEquals(-4, box.getMinY());
        assertEquals(5, box.getMaxX());
        assertEquals(2, box.getMaxY());
    }

    @Test
    void coversMatchesRadiusTolerance() {
        CircleShape circle = new CircleShape("c", 0, 0, 1);
        assertTrue(circle.covers(new Point2D(1, 0), 0.05));
        assertTrue(circle.covers(new Point2D(0.98, 0), 0.05));
        assertFalse(circle.covers(new Point2D(0.9, 0), 0.05));
    }

    @Test
    void moveShiftsCenter() {
        CircleShape circle = new CircleShape("c", 1, 2, 1);
        circle.move(-1, 3);
        assertEquals(0, circle.getCenterX());
        assertEquals(5, circle.getCenterY());
    }
}
