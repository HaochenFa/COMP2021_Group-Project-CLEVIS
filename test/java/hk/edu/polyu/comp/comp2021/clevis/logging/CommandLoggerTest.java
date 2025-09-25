package hk.edu.polyu.comp.comp2021.clevis.logging;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CommandLoggerTest {

    @Test
    void writesCommandsToHtmlAndText() throws IOException {
        Path html = Files.createTempFile("clevis", ".html");
        Path txt = Files.createTempFile("clevis", ".txt");
        try (CommandLogger logger = new CommandLogger(html, txt)) {
            logger.log("rectangle r 0 0 1 1");
            logger.log("quit");
        }
        String htmlContent = Files.readString(html);
        String textContent = Files.readString(txt);
        assertTrue(htmlContent.contains("<tr><td>1</td><td>rectangle r 0 0 1 1</td></tr>"));
        assertTrue(htmlContent.contains("</table>"));
        assertEquals("rectangle r 0 0 1 1\nquit\n", textContent);
        Files.deleteIfExists(html);
        Files.deleteIfExists(txt);
    }

    @Test
    void throwsIfLoggingAfterClose() throws IOException {
        Path html = Files.createTempFile("clevis", ".html");
        Path txt = Files.createTempFile("clevis", ".txt");
        CommandLogger logger = new CommandLogger(html, txt);
        logger.close();
        assertThrows(IllegalStateException.class, () -> logger.log("rectangle r 0 0 1 1"));
        Files.deleteIfExists(html);
        Files.deleteIfExists(txt);
    }
}
