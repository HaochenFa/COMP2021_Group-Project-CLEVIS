package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareShapeTest {

    @Test
    void constructorRequiresPositiveSide() {
        assertThrows(IllegalArgumentException.class, () -> new SquareShape("sq", 0, 0, 0));
    }

    @Test
    void inheritsRectangleBehaviour() {
        SquareShape square = new SquareShape("sq", 1, 1, 3);
        BoundingBox box = square.getBoundingBox();
        assertEquals(1, box.getMinX());
        assertEquals(1, box.getMinY());
        assertEquals(4, box.getMaxX());
        assertEquals(4, box.getMaxY());
    }
}
