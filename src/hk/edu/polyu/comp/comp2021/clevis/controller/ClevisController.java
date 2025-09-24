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

    public ClevisController(ShapeRepository repository, CommandLogger logger, ConsoleView view) {
        this.repository = repository;
        this.logger = logger;
        this.view = view;
    }

    public boolean isRunning() {
        return running;
    }

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
                case "rectangle":
                    handleRectangle(tokens);
                    break;
                case "line":
                    handleLine(tokens);
                    break;
                case "circle":
                    handleCircle(tokens);
                    break;
                case "square":
                    handleSquare(tokens);
                    break;
                case "group":
                    handleGroup(tokens);
                    break;
                case "ungroup":
                    handleUngroup(tokens);
                    break;
                case "delete":
                    handleDelete(tokens);
                    break;
                case "boundingbox":
                    handleBoundingBox(tokens);
                    break;
                case "move":
                    handleMove(tokens);
                    break;
                case "shapeat":
                    handleShapeAt(tokens);
                    break;
                case "intersect":
                    handleIntersect(tokens);
                    break;
                case "list":
                    handleList(tokens);
                    break;
                case "listall":
                    handleListAll(tokens);
                    break;
                case "quit":
                    running = false;
                    break;
                default:
                    view.showError("Unknown command: " + keyword);
                    break;
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            view.showError(ex.getMessage());
        } finally {
            logger.flush();
        }
    }

    private void handleRectangle(String[] tokens) {
        ensureLength(tokens, 6);
        String name = tokens[1];
        double x = parseDouble(tokens[2]);
        double y = parseDouble(tokens[3]);
        double width = parseDouble(tokens[4]);
        double height = parseDouble(tokens[5]);
        repository.registerShape(new RectangleShape(name, x, y, width, height));
    }

    private void handleSquare(String[] tokens) {
        ensureLength(tokens, 5);
        String name = tokens[1];
        double x = parseDouble(tokens[2]);
        double y = parseDouble(tokens[3]);
        double side = parseDouble(tokens[4]);
        repository.registerShape(new SquareShape(name, x, y, side));
    }

    private void handleCircle(String[] tokens) {
        ensureLength(tokens, 5);
        String name = tokens[1];
        double x = parseDouble(tokens[2]);
        double y = parseDouble(tokens[3]);
        double radius = parseDouble(tokens[4]);
        repository.registerShape(new CircleShape(name, x, y, radius));
    }

    private void handleLine(String[] tokens) {
        ensureLength(tokens, 6);
        String name = tokens[1];
        double x1 = parseDouble(tokens[2]);
        double y1 = parseDouble(tokens[3]);
        double x2 = parseDouble(tokens[4]);
        double y2 = parseDouble(tokens[5]);
        repository.registerShape(new LineSegmentShape(name, x1, y1, x2, y2));
    }

    private void handleGroup(String[] tokens) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Group command requires a name and members");
        }
        String groupName = tokens[1];
        List<String> members = new ArrayList<String>(Arrays.asList(tokens).subList(2, tokens.length));
        repository.groupShapes(groupName, members);
    }

    private void handleUngroup(String[] tokens) {
        ensureLength(tokens, 2);
        repository.ungroup(tokens[1]);
    }

    private void handleDelete(String[] tokens) {
        ensureLength(tokens, 2);
        repository.deleteShape(tokens[1]);
    }

    private void handleBoundingBox(String[] tokens) {
        ensureLength(tokens, 2);
        Shape shape = repository.requireShape(tokens[1]);
        BoundingBox box = shape.getBoundingBox();
        view.showBoundingBox(box);
    }

    private void handleMove(String[] tokens) {
        ensureLength(tokens, 4);
        Shape shape = repository.requireTopLevelShape(tokens[1]);
        double dx = parseDouble(tokens[2]);
        double dy = parseDouble(tokens[3]);
        shape.move(dx, dy);
    }

    private void handleShapeAt(String[] tokens) {
        ensureLength(tokens, 3);
        double x = parseDouble(tokens[1]);
        double y = parseDouble(tokens[2]);
        Optional<Shape> shape = repository.findTopShapeAt(new Point2D(x, y), SHAPE_AT_TOLERANCE);
        view.showMessage(shape.map(Shape::getName).orElse("NONE"));
    }

    private void handleIntersect(String[] tokens) {
        ensureLength(tokens, 3);
        Shape first = repository.requireShape(tokens[1]);
        Shape second = repository.requireShape(tokens[2]);
        boolean intersects = first.getBoundingBox().intersects(second.getBoundingBox());
        view.showMessage(Boolean.toString(intersects));
    }

    private void handleList(String[] tokens) {
        ensureLength(tokens, 2);
        Shape shape = repository.requireShape(tokens[1]);
        view.showMessage(formatShapeDescription(shape));
    }

    private void handleListAll(String[] tokens) {
        ensureLength(tokens, 1);
        List<Shape> shapes = repository.listTopLevelShapes();
        for (int i = shapes.size() - 1; i >= 0; i--) {
            listShapeRecursive(shapes.get(i), 0);
        }
    }

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

    private String formatShapeDescription(Shape shape) {
        if (shape instanceof RectangleShape && !(shape instanceof SquareShape)) {
            RectangleShape rectangle = (RectangleShape) shape;
            return String.format(Locale.US, "rectangle %s %s %s %s %s",
                rectangle.getName(),
                view.formatDouble(rectangle.getX()),
                view.formatDouble(rectangle.getY()),
                view.formatDouble(rectangle.getWidth()),
                view.formatDouble(rectangle.getHeight()));
        }
        if (shape instanceof SquareShape) {
            SquareShape square = (SquareShape) shape;
            return String.format(Locale.US, "square %s %s %s %s",
                square.getName(),
                view.formatDouble(square.getX()),
                view.formatDouble(square.getY()),
                view.formatDouble(square.getWidth()));
        }
        if (shape instanceof CircleShape) {
            CircleShape circle = (CircleShape) shape;
            return String.format(Locale.US, "circle %s %s %s %s",
                circle.getName(),
                view.formatDouble(circle.getCenterX()),
                view.formatDouble(circle.getCenterY()),
                view.formatDouble(circle.getRadius()));
        }
        if (shape instanceof LineSegmentShape) {
            LineSegmentShape line = (LineSegmentShape) shape;
            return String.format(Locale.US, "line %s %s %s %s %s",
                line.getName(),
                view.formatDouble(line.getStart().getX()),
                view.formatDouble(line.getStart().getY()),
                view.formatDouble(line.getEnd().getX()),
                view.formatDouble(line.getEnd().getY()));
        }
        if (shape instanceof GroupShape) {
            GroupShape group = (GroupShape) shape;
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

    private static void ensureLength(String[] tokens, int expected) {
        if (tokens.length != expected) {
            throw new IllegalArgumentException("Expected " + expected + " arguments, got " + tokens.length);
        }
    }

    private static double parseDouble(String token) {
        return Double.parseDouble(token);
    }
}
