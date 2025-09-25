/**
 * @author FA, Haochen 24113347D
 * @date_created 25th Sep, 2025
 * @latest_update N/A
 * @description Command Logger writing both HTML and text audit trails.
 */

package hk.edu.polyu.comp.comp2021.clevis.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Logs user commands into HTML and plain text outputs as required by REQ1.
 */
public final class CommandLogger implements AutoCloseable {
    private final BufferedWriter htmlWriter;
    private final BufferedWriter textWriter;
    private int counter;
    private boolean closed;

    /**
     * @constructor
     *
     * @param htmlPath Destination for HTML log output
     * @param textPath Destination for plain-text log output
     * @throws IOException When writers cannot be created
     */
    public CommandLogger(Path htmlPath, Path textPath) throws IOException {
        htmlWriter = Files.newBufferedWriter(htmlPath, StandardCharsets.UTF_8);
        textWriter = Files.newBufferedWriter(textPath, StandardCharsets.UTF_8);
        writeHtmlHeader();
    }

    /**
     * @function `log` Records a command in both outputs.
     *
     * @param command Raw command string
     */
    public void log(String command) {
        if (closed) {
            throw new IllegalStateException("Logger already closed");
        }
        counter++;
        try {
            htmlWriter.write("  <tr><td>" + counter + "</td><td>" + escape(command) + "</td></tr>\n");
            textWriter.write(command);
            textWriter.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to log command", e);
        }
    }

    /**
     * @function `flush` Forces buffered content to disk for both writers.
     */
    public void flush() {
        if (closed) {
            return;
        }
        try {
            htmlWriter.flush();
            textWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to flush logs", e);
        }
    }

    /**
     * @function `close` Finalizes logs, writing the HTML footer and closing
     *           streams.
     */
    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        try (BufferedWriter htmlResource = htmlWriter; BufferedWriter textResource = textWriter) {
            writeHtmlFooter();
            htmlResource.flush();
            textResource.flush();
        } catch (IOException e) {
            throw new IOException("Failed to close command logs", e);
        }
    }

    private void writeHtmlHeader() throws IOException {
        htmlWriter.write("<html>\n<head><meta charset=\"utf-8\"><title>Clevis Log</title></head>\n");
        htmlWriter.write("<body>\n<table border=\"1\">\n");
        htmlWriter.write("  <tr><th>#</th><th>Command</th></tr>\n");
    }

    private void writeHtmlFooter() throws IOException {
        htmlWriter.write("</table>\n</body>\n</html>\n");
    }

    private static String escape(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
