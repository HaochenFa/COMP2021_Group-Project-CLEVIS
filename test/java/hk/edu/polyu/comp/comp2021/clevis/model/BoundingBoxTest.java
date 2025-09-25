package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundingBoxTest {

    @Test
    void createsBoxFromCoordinates() {
        BoundingBox box = BoundingBox.of(1.0, 2.0, 3.5, 6.0);
        assertEquals(1.0, box.getMinX());
        assertEquals(2.0, box.getMinY());
        assertEquals(3.5, box.getMaxX());
        assertEquals(6.0, box.getMaxY());
        assertEquals(2.5, box.getWidth());
        assertEquals(4.0, box.getHeight());
    }

    @Test
    void throwsWhenBoundsInverted() {
        assertThrows(IllegalArgumentException.class, () -> BoundingBox.of(5, 0, 2, 10));
        assertThrows(IllegalArgumentException.class, () -> BoundingBox.of(0, 5, 2, 3));
    }

    @Test
    void buildsFromPointsIndependentOfOrder() {
        Point2D first = new Point2D(4, -2);
        Point2D second = new Point2D(-1, 5);
        BoundingBox box = BoundingBox.fromPoints(first, second);
        assertEquals(-1, box.getMinX());
        assertEquals(-2, box.getMinY());
        assertEquals(4, box.getMaxX());
        assertEquals(5, box.getMaxY());
    }

    @Test
    void expandsToIncludeOtherBoxes() {
        BoundingBox base = BoundingBox.of(0, 0, 1, 1);
        BoundingBox expanded = base.expandToInclude(BoundingBox.of(-2, 0.5, 2, 2));
        assertEquals(-2, expanded.getMinX());
        assertEquals(0, expanded.getMinY());
        assertEquals(2, expanded.getMaxX());
        assertEquals(2, expanded.getMaxY());
    }

    @Test
    void intersectsExcludesEdgesOnlyContact() {
        BoundingBox base = BoundingBox.of(0, 0, 2, 2);
        assertTrue(base.intersects(BoundingBox.of(1, 1, 3, 3)));
        assertFalse(base.intersects(BoundingBox.of(2, -1, 4, 1)));
        assertFalse(base.intersects(BoundingBox.of(-1, 2, 1, 4)));
    }
}
