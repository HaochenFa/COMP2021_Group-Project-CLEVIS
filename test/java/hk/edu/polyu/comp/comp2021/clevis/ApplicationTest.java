package hk.edu.polyu.comp.comp2021.clevis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {
    private PrintStream originalErr;
    private ByteArrayOutputStream errBuffer;

    @BeforeEach
    void setUp() {
        originalErr = System.err;
        errBuffer = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errBuffer));
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    void missingArgumentsPrintsUsage() {
        Application.main(new String[]{"-html", "log.html"});
        String output = errBuffer.toString();
        assertTrue(output.contains("Both -html and -txt must be specified"));
        assertTrue(output.contains("Usage:"));
    }

    @Test
    void unknownArgumentIsReported() {
        Application.main(new String[]{"-foo"});
        String output = errBuffer.toString();
        assertTrue(output.contains("Unknown argument"));
    }
}
