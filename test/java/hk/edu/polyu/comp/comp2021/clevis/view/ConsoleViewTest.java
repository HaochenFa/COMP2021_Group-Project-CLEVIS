package hk.edu.polyu.comp.comp2021.clevis.view;

import hk.edu.polyu.comp.comp2021.clevis.model.BoundingBox;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleViewTest {

    @Test
    void formatDoubleRoundsToTwoDecimals() {
        ConsoleView view = createView();
        assertEquals("1.23", view.formatDouble(1.234));
        assertEquals("-0.58", view.formatDouble(-0.575));
    }

    @Test
    void showBoundingBoxPrintsFormattedValues() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ConsoleView view = new ConsoleView(new PrintStream(out), System.err);
        view.showBoundingBox(BoundingBox.of(0, 1, 3.1415, 4.2));
        assertEquals("0.00 1.00 3.14 3.20\n", out.toString());
    }

    private ConsoleView createView() {
        return new ConsoleView(new PrintStream(new ByteArrayOutputStream()), System.err);
    }
}
