/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Clevis Controller orchestrating command parsing & execution.
 */

package hk.edu.polyu.comp.comp2021.clevis.controller;

import hk.edu.polyu.comp.comp2021.clevis.logging.CommandLogger;
import hk.edu.polyu.comp.comp2021.clevis.model.BoundingBox;
import hk.edu.polyu.comp.comp2021.clevis.model.CircleShape;
import hk.edu.polyu.comp.comp2021.clevis.model.GroupShape;
import hk.edu.polyu.comp.comp2021.clevis.model.LineSegmentShape;
import hk.edu.polyu.comp.comp2021.clevis.model.Point2D;
import hk.edu.polyu.comp.comp2021.clevis.model.RectangleShape;
import hk.edu.polyu.comp.comp2021.clevis.model.Shape;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeRepository;
import hk.edu.polyu.comp.comp2021.clevis.model.SquareShape;
import hk.edu.polyu.comp.comp2021.clevis.view.ConsoleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Core Clevis command dispatcher.
 */
public final class ClevisController {
    private static final double SHAPE_AT_TOLERANCE = 0.05;

    private final ShapeRepository repository;
    private final CommandLogger logger;
    private final ConsoleView view;
    private boolean running = true;

    /**
     * @constructor
     *
     * @param repository Shape storage abstraction
     * @param logger     Command logger
     * @param view       CLI view used for all output
     */
    public ClevisController(ShapeRepository repository, CommandLogger logger, ConsoleView view) {
        this.repository = repository;
        this.logger = logger;
        this.view = view;
    }

    /**
     * @function `isRunning`
     *
     * @return Flag indicating whether CLI loop should continue
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @function `executeCommand` Parses and executes a single user command.
     *
     * @param rawCommand Raw command string from the CLI
     */
    public void executeCommand(String rawCommand) {
        String command = rawCommand.trim();
        if (command.isEmpty()) {
            return;
        }
        logger.log(command);
        String[] tokens = command.split("\\s+");
        String keyword = tokens[0].toLowerCase(Locale.ROOT);
        try {
            switch (keyword) {
                case "rectangle" -> handleRectangle(tokens);
                case "line" -> handleLine(tokens);
                case "circle" -> handleCircle(tokens);
                case "square" -> handleSquare(tokens);
                case "group" -> handleGroup(tokens);
                case "ungroup" -> handleUngroup(tokens);
                case "delete" -> handleDelete(tokens);
                case "boundingbox" -> handleBoundingBox(tokens);
                case "move" -> handleMove(tokens);
                case "shapeat" -> handleShapeAt(tokens);
                case "intersect" -> handleIntersect(tokens);
                case "list" -> handleList(tokens);
                case "listall" -> handleListAll(tokens);
                case "quit" -> running = false;
                default -> view.showError("Unknown command: " + keyword);
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            view.showError(ex.getMessage());
        } finally {
            logger.flush();
        }
    }

    /**
     * @function `handleRectangle` Creates a rectangle shape.
     *
     * @param tokens Command tokens
     */
    private void handleRectangle(String[] tokens) {
        ensureLength(tokens, 6);
        String name = tokens[1];
        double x = parseDouble(tokens[2]);
        double y = parseDouble(tokens[3]);
        double width = parseDouble(tokens[4]);
        double height = parseDouble(tokens[5]);
        repository.registerShape(new RectangleShape(name, x, y, width, height));
    }

    /**
     * @function `handleSquare` Creates a square shape.
     *
     * @param tokens Command tokens
     */
    private void handleSquare(String[] tokens) {
        ensureLength(tokens, 5);
        String name = tokens[1];
        double x = parseDouble(tokens[2]);
        double y = parseDouble(tokens[3]);
        double side = parseDouble(tokens[4]);
        repository.registerShape(new SquareShape(name, x, y, side));
    }

    /**
     * @function `handleCircle` Creates a circle shape.
     *
     * @param tokens Command tokens
     */
    private void handleCircle(String[] tokens) {
        ensureLength(tokens, 5);
        String name = tokens[1];
        double x = parseDouble(tokens[2]);
        double y = parseDouble(tokens[3]);
        double radius = parseDouble(tokens[4]);
        repository.registerShape(new CircleShape(name, x, y, radius));
    }

    /**
     * @function `handleLine` Creates a line segment.
     *
     * @param tokens Command tokens
     */
    private void handleLine(String[] tokens) {
        ensureLength(tokens, 6);
        String name = tokens[1];
        double x1 = parseDouble(tokens[2]);
        double y1 = parseDouble(tokens[3]);
        double x2 = parseDouble(tokens[4]);
        double y2 = parseDouble(tokens[5]);
        repository.registerShape(new LineSegmentShape(name, x1, y1, x2, y2));
    }

    /**
     * @function `handleGroup` Groups multiple shapes under a new name.
     *
     * @param tokens Command tokens
     */
    private void handleGroup(String[] tokens) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Group command requires a name and members");
        }
        String groupName = tokens[1];
        List<String> members = new ArrayList<>(Arrays.asList(tokens).subList(2, tokens.length));
        repository.groupShapes(groupName, members);
    }

    /**
     * @function `handleUngroup` Restores members of an existing group.
     *
     * @param tokens Command tokens
     */
    private void handleUngroup(String[] tokens) {
        ensureLength(tokens, 2);
        repository.ungroup(tokens[1]);
    }

    /**
     * @function `handleDelete` Removes a top-level shape.
     *
     * @param tokens Command tokens
     */
    private void handleDelete(String[] tokens) {
        ensureLength(tokens, 2);
        repository.deleteShape(tokens[1]);
    }

    /**
     * @function `handleBoundingBox` Displays the bounding box of a shape.
     *
     * @param tokens Command tokens
     */
    private void handleBoundingBox(String[] tokens) {
        ensureLength(tokens, 2);
        Shape shape = repository.requireShape(tokens[1]);
        BoundingBox box = shape.getBoundingBox();
        view.showBoundingBox(box);
    }

    /**
     * @function `handleMove` Translates a top-level shape.
     *
     * @param tokens Command tokens
     */
    private void handleMove(String[] tokens) {
        ensureLength(tokens, 4);
        Shape shape = repository.requireTopLevelShape(tokens[1]);
        double dx = parseDouble(tokens[2]);
        double dy = parseDouble(tokens[3]);
        shape.move(dx, dy);
    }

    /**
     * @function `handleShapeAt` Finds the top-most shape at coordinates.
     *
     * @param tokens Command tokens
     */
    private void handleShapeAt(String[] tokens) {
        ensureLength(tokens, 3);
        double x = parseDouble(tokens[1]);
        double y = parseDouble(tokens[2]);
        Optional<Shape> shape = repository.findTopShapeAt(new Point2D(x, y), SHAPE_AT_TOLERANCE);
        view.showMessage(shape.map(Shape::getName).orElse("NONE"));
    }

    /**
     * @function `handleIntersect` Performs bounding-box intersection check.
     *
     * @param tokens Command tokens
     */
    private void handleIntersect(String[] tokens) {
        ensureLength(tokens, 3);
        Shape first = repository.requireShape(tokens[1]);
        Shape second = repository.requireShape(tokens[2]);
        boolean intersects = first.getBoundingBox().intersects(second.getBoundingBox());
        view.showMessage(Boolean.toString(intersects));
    }

    /**
     * @function `handleList` Displays a description of a shape.
     *
     * @param tokens Command tokens
     */
    private void handleList(String[] tokens) {
        ensureLength(tokens, 2);
        Shape shape = repository.requireShape(tokens[1]);
        view.showMessage(formatShapeDescription(shape));
    }

    /**
     * @function `handleListAll` Lists all top-level shapes in z-order.
     *
     * @param tokens Command tokens
     */
    private void handleListAll(String[] tokens) {
        ensureLength(tokens, 1);
        List<Shape> shapes = repository.listTopLevelShapes();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            listShapeRecursive(shapes.get(i), 0);
        }
    }

    /**
     * @function `listShapeRecursive` Recursively prints nested group contents.
     *
     * @param shape Shape to describe
     * @param depth Current indentation depth
     */
    private void listShapeRecursive(Shape shape, int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append("  ");
        }
        builder.append(formatShapeDescription(shape));
        view.showMessage(builder.toString());
        if (shape.isGroup()) {
            GroupShape group = (GroupShape) shape;
            List<Shape> children = group.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                listShapeRecursive(children.get(i), depth + 1);
            }
        }
    }

    /**
     * @function `formatShapeDescription` Serializes a shape into CLI-friendly
     *           string.
     *
     * @param shape Shape instance
     * @return Description string
     */
    private String formatShapeDescription(Shape shape) {
        Objects.requireNonNull(shape, "shape");
        if (shape instanceof SquareShape square) {
            return String.format(Locale.US, "square %s %s %s %s",
                    square.getName(),
                    view.formatDouble(square.getX()),
                    view.formatDouble(square.getY()),
                    view.formatDouble(square.getWidth()));
        }
        if (shape instanceof RectangleShape rectangle) {
            return String.format(Locale.US, "rectangle %s %s %s %s %s",
                    rectangle.getName(),
                    view.formatDouble(rectangle.getX()),
                    view.formatDouble(rectangle.getY()),
                    view.formatDouble(rectangle.getWidth()),
                    view.formatDouble(rectangle.getHeight()));
        }
        if (shape instanceof CircleShape circle) {
            return String.format(Locale.US, "circle %s %s %s %s",
                    circle.getName(),
                    view.formatDouble(circle.getCenterX()),
                    view.formatDouble(circle.getCenterY()),
                    view.formatDouble(circle.getRadius()));
        }
        if (shape instanceof LineSegmentShape line) {
            return String.format(Locale.US, "line %s %s %s %s %s",
                    line.getName(),
                    view.formatDouble(line.getStart().getX()),
                    view.formatDouble(line.getStart().getY()),
                    view.formatDouble(line.getEnd().getX()),
                    view.formatDouble(line.getEnd().getY()));
        }
        if (shape instanceof GroupShape group) {
            StringBuilder builder = new StringBuilder();
            builder.append("group ").append(group.getName()).append(" [");
            List<Shape> children = group.getChildren();
            for (int i = 0; i < children.size(); i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(children.get(i).getName());
            }
            builder.append("]");
            return builder.toString();
        }
        return shape.getName();
    }

    /**
     * @function `ensureLength` Validates command length.
     *
     * @param tokens   Command tokens
     * @param expected Expected token count
     */
    private static void ensureLength(String[] tokens, int expected) {
        if (tokens.length != expected) {
            throw new IllegalArgumentException("Expected " + expected + " arguments, got " + tokens.length);
        }
    }

    /**
     * @function `parseDouble`
     *
     * @param token Numeric token
     * @return Parsed double value
     */
    private static double parseDouble(String token) {
        return Double.parseDouble(token);
    }
}
