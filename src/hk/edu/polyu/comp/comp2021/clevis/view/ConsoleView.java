package hk.edu.polyu.comp.comp2021.clevis.view;

import hk.edu.polyu.comp.comp2021.clevis.model.BoundingBox;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * CLI rendering helper that keeps output formatting consistent.
 */
public final class ConsoleView {
    private final PrintStream out;
    private final PrintStream err;
    private final DecimalFormat decimalFormat;

    public ConsoleView(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        this.decimalFormat = new DecimalFormat("0.00", symbols);
    }

    public void showMessage(String message) {
        out.println(message);
    }

    public void showError(String message) {
        err.println(message);
    }

    public void showBoundingBox(BoundingBox box) {
        out.println(formatDouble(box.getMinX()) + " " +
                formatDouble(box.getMinY()) + " " +
                formatDouble(box.getWidth()) + " " +
                formatDouble(box.getHeight()));
    }

    public String formatDouble(double value) {
        return decimalFormat.format(value);
    }
}
