package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Base class with shared name handling for all shapes.
 */
public abstract class AbstractShape implements Shape {
    private final String name;

    protected AbstractShape(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Shape name must not be empty");
        }
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }
}
