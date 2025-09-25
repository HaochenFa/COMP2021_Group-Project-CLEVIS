package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShapeRepositoryTest {

    @Test
    void registerAddsShapeAndPreventsDuplicates() {
        ShapeRepository repository = new ShapeRepository();
        RectangleShape rect = new RectangleShape("r", 0, 0, 1, 1);
        repository.registerShape(rect);
        assertEquals(List.of(rect), repository.listTopLevelShapes());
        assertThrows(IllegalArgumentException.class, () -> repository.registerShape(rect));
    }

    @Test
    void groupRemovesMembersFromTopLevelAndSetsParent() {
        ShapeRepository repository = new ShapeRepository();
        RectangleShape rect = new RectangleShape("r", 0, 0, 1, 1);
        CircleShape circle = new CircleShape("c", 2, 2, 1);
        repository.registerShape(rect);
        repository.registerShape(circle);
        GroupShape group = repository.groupShapes("g", List.of("r", "c"));
        assertEquals(List.of(group), repository.listTopLevelShapes());
        assertThrows(IllegalStateException.class, () -> repository.requireTopLevelShape("r"));
    }

    @Test
    void ungroupRestoresMembersAndRemovesGroup() {
        ShapeRepository repository = new ShapeRepository();
        RectangleShape rect = new RectangleShape("r", 0, 0, 1, 1);
        CircleShape circle = new CircleShape("c", 2, 2, 1);
        repository.registerShape(rect);
        repository.registerShape(circle);
        repository.groupShapes("g", List.of("r", "c"));
        List<Shape> restored = repository.ungroup("g");
        assertEquals(2, restored.size());
        assertTrue(repository.isTopLevel("r"));
        assertEquals(2, repository.listTopLevelShapes().size());
    }

    @Test
    void deleteRemovesGroupsRecursively() {
        ShapeRepository repository = new ShapeRepository();
        RectangleShape rect = new RectangleShape("r", 0, 0, 1, 1);
        CircleShape circle = new CircleShape("c", 2, 2, 1);
        repository.registerShape(rect);
        repository.registerShape(circle);
        repository.groupShapes("g", List.of("r", "c"));
        repository.deleteShape("g");
        assertTrue(repository.listTopLevelShapes().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> repository.requireShape("r"));
    }

    @Test
    void findTopShapeAtRespectsZOrder() {
        ShapeRepository repository = new ShapeRepository();
        RectangleShape rect = new RectangleShape("r", 0, 0, 2, 2);
        RectangleShape rect2 = new RectangleShape("r2", 0, 0, 2, 2);
        repository.registerShape(rect);
        repository.registerShape(rect2);
        assertEquals("r2", repository.findTopShapeAt(new Point2D(1, 0), 0.05).map(Shape::getName).orElseThrow());
    }

    @Test
    void requireTopLevelShapeRejectsGroupedMembers() {
        ShapeRepository repository = new ShapeRepository();
        RectangleShape rect = new RectangleShape("r", 0, 0, 1, 1);
        CircleShape circle = new CircleShape("c", 2, 2, 1);
        repository.registerShape(rect);
        repository.registerShape(circle);
        repository.groupShapes("g", List.of("r", "c"));
        assertThrows(IllegalStateException.class, () -> repository.requireTopLevelShape("c"));
    }
}
