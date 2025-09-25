package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineSegmentShapeTest {

    @Test
    void constructorRequiresDistinctEndpoints() {
        assertThrows(IllegalArgumentException.class, () -> new LineSegmentShape("l", 1, 1, 1, 1));
    }

    @Test
    void boundingBoxAlignsWithEndpoints() {
        LineSegmentShape line = new LineSegmentShape("l", -1, -2, 3, 4);
        BoundingBox box = line.getBoundingBox();
        assertEquals(-1, box.getMinX());
        assertEquals(-2, box.getMinY());
        assertEquals(3, box.getMaxX());
        assertEquals(4, box.getMaxY());
    }

    @Test
    void moveTranslatesEndpoints() {
        LineSegmentShape line = new LineSegmentShape("l", 0, 0, 1, 1);
        line.move(2, -1);
        assertEquals(2, line.getStart().getX());
        assertEquals(-1, line.getStart().getY());
        assertEquals(3, line.getEnd().getX());
        assertEquals(0, line.getEnd().getY());
    }

    @Test
    void coversDetectsNearSegmentPoints() {
        LineSegmentShape line = new LineSegmentShape("l", 0, 0, 2, 0);
        assertTrue(line.covers(new Point2D(1, 0.01), 0.05));
        assertFalse(line.covers(new Point2D(1, 0.2), 0.05));
    }
}
