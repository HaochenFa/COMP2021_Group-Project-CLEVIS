package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleShapeTest {

    @Test
    void constructorRejectsNonPositiveDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new RectangleShape("r", 0, 0, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new RectangleShape("r", 0, 0, 1, -1));
    }

    @Test
    void boundingBoxMatchesCoordinates() {
        RectangleShape rectangle = new RectangleShape("rect", 2, 5, 3, 4);
        BoundingBox box = rectangle.getBoundingBox();
        assertEquals(2, box.getMinX());
        assertEquals(5, box.getMinY());
        assertEquals(5, box.getMaxX());
        assertEquals(9, box.getMaxY());
    }

    @Test
    void moveTranslatesRectangle() {
        RectangleShape rectangle = new RectangleShape("rect", 0, 0, 2, 3);
        rectangle.move(1.5, -2);
        BoundingBox box = rectangle.getBoundingBox();
        assertEquals(1.5, box.getMinX());
        assertEquals(-2, box.getMinY());
    }

    @Test
    void coversReturnsTrueNearEdges() {
        RectangleShape rectangle = new RectangleShape("rect", 0, 0, 2, 2);
        Point2D onEdge = new Point2D(1, 0);
        Point2D nearlyEdge = new Point2D(1, 0.04);
        Point2D outside = new Point2D(1, 0.2);
        assertTrue(rectangle.covers(onEdge, 0.05));
        assertTrue(rectangle.covers(nearlyEdge, 0.05));
        assertFalse(rectangle.covers(outside, 0.05));
    }
}
