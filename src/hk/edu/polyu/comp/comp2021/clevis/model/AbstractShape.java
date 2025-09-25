/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Base Class for All Shapes.
 */

package hk.edu.polyu.comp.comp2021.clevis.model;

/**
 * Base class with shared name handling for all shapes.
 */
public abstract class AbstractShape implements Shape {
    private final String name;

    /**
     * @constructor
     *
     * @param name Unique shape identifier
     */
    protected AbstractShape(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Shape name must not be empty");
        }
        this.name = name;
    }

    /**
     * @function `getName`
     *
     * @return Stored shape name
     */
    @Override
    public final String getName() {
        return name;
    }
}
