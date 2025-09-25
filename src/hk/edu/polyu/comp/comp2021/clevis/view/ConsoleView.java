/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update 26th Sep, 2025 by FA, Haochen
 * @description Console View for formatting Clevis CLI output.
 */

package hk.edu.polyu.comp.comp2021.clevis.view;

import hk.edu.polyu.comp.comp2021.clevis.model.BoundingBox;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CLI rendering helper that keeps output formatting consistent.
 */
public final class ConsoleView {
    private final PrintStream out;
    private final PrintStream err;

    /**
     * @constructor
     *
     * @param out Standard output stream
     * @param err Error output stream
     */
    public ConsoleView(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    /**
     * @function `showMessage`
     *
     * @param message Text to print on standard output
     */
    public void showMessage(String message) {
        out.println(message);
    }

    /**
     * @function `showError`
     *
     * @param message Text to print on error output
     */
    public void showError(String message) {
        err.println(message);
    }

    /**
     * @function `showBoundingBox` Prints bbox coordinates in formatted order.
     *
     * @param box Bounding box instance
     */
    public void showBoundingBox(BoundingBox box) {
        out.println(formatDouble(box.getMinX()) + " " +
                formatDouble(box.getMinY()) + " " +
                formatDouble(box.getWidth()) + " " +
                formatDouble(box.getHeight()));
    }

    /**
     * @function `formatDouble` Applies CLI numeric formatting.
     *
     * @param value Numeric value
     * @return Formatted decimal string
     */
    public String formatDouble(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
        return decimal.toPlainString();
    }
}
