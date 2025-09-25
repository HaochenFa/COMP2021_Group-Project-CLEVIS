package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Point2DTest {

    @Test
    void storesCoordinates() {
        Point2D point = new Point2D(3.5, -7.25);
        assertEquals(3.5, point.getX());
        assertEquals(-7.25, point.getY());
    }

    @Test
    void translateProducesNewInstance() {
        Point2D point = new Point2D(1, 2);
        Point2D translated = point.translate(0.5, -0.25);
        assertEquals(1.5, translated.getX());
        assertEquals(1.75, translated.getY());
        assertEquals(1, point.getX());
        assertEquals(2, point.getY());
    }
}
