package hk.edu.polyu.comp.comp2021.clevis.controller;

import hk.edu.polyu.comp.comp2021.clevis.logging.CommandLogger;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeRepository;
import hk.edu.polyu.comp.comp2021.clevis.view.ConsoleView;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClevisControllerTest {

    @Test
    void handlesShapeCreationAndListing() throws Exception {
        TestHarness harness = new TestHarness();
        harness.run(
                "rectangle rect 0 0 2 2",
                "circle circ 0 0 1",
                "listall"
        );
        List<String> outputs = harness.getStdoutLines();
        assertTrue(outputs.contains("circle circ 0.00 0.00 1.00"));
        assertTrue(outputs.contains("rectangle rect 0.00 0.00 2.00 2.00"));
        harness.close();
    }

    @Test
    void reportsErrorsWhenUsingGroupedMembers() throws Exception {
        TestHarness harness = new TestHarness();
        harness.run(
                "rectangle r1 0 0 1 1",
                "rectangle r2 2 2 1 1",
                "group g r1 r2",
                "boundingbox r1"
        );
        String stderr = harness.getStderr();
        assertTrue(stderr.contains("Shape is currently grouped"));
        harness.close();
    }

    @Test
    void shapeAtReturnsTopMostShape() throws Exception {
        TestHarness harness = new TestHarness();
        harness.run(
                "rectangle base 0 0 2 2",
                "rectangle top 0 0 2 2",
                "shapeAt 1 0.01"
        );
        List<String> outputs = harness.getStdoutLines();
        assertEquals("top", outputs.get(outputs.size() - 1));
        harness.close();
    }

    @Test
    void intersectUsesBoundingBoxesForGroups() throws Exception {
        TestHarness harness = new TestHarness();
        harness.run(
                "rectangle r1 0 0 2 2",
                "rectangle r2 5 5 2 2",
                "group g r1",
                "rectangle r3 1 1 1 1",
                "intersect g r3"
        );
        List<String> outputs = harness.getStdoutLines();
        assertEquals("true", outputs.get(outputs.size() - 1));
        harness.close();
    }

    private static final class TestHarness implements AutoCloseable {
        private final Path html;
        private final Path txt;
        private final ByteArrayOutputStream stdoutBuffer = new ByteArrayOutputStream();
        private final ByteArrayOutputStream stderrBuffer = new ByteArrayOutputStream();
        private final CommandLogger logger;
        private final ClevisController controller;

        private TestHarness() throws Exception {
            html = Files.createTempFile("clevis-test", ".html");
            txt = Files.createTempFile("clevis-test", ".txt");
            logger = new CommandLogger(html, txt);
            ConsoleView view = new ConsoleView(new PrintStream(stdoutBuffer), new PrintStream(stderrBuffer));
            controller = new ClevisController(new ShapeRepository(), logger, view);
        }

        private void run(String... commands) {
            Arrays.stream(commands).forEach(controller::executeCommand);
        }

        private List<String> getStdoutLines() {
            return Arrays.stream(stdoutBuffer.toString().split("\n"))
                    .filter(line -> !line.isEmpty())
                    .toList();
        }

        private String getStderr() {
            return stderrBuffer.toString();
        }

        @Override
        public void close() throws Exception {
            logger.close();
            Files.deleteIfExists(html);
            Files.deleteIfExists(txt);
        }
    }
}
