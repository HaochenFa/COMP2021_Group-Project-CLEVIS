package hk.edu.polyu.comp.comp2021.clevis;

import hk.edu.polyu.comp.comp2021.clevis.controller.ClevisController;
import hk.edu.polyu.comp.comp2021.clevis.logging.CommandLogger;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeRepository;
import hk.edu.polyu.comp.comp2021.clevis.view.ConsoleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Clevis CLI entry point.
 */
public final class Application {
    private Application() {
    }

    public static void main(String[] args) {
        StartupArguments startupArguments;
        try {
            startupArguments = parseArguments(args);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            printUsage();
            return;
        }

        ConsoleView view = new ConsoleView(System.out, System.err);
        ShapeRepository repository = new ShapeRepository();

        try (CommandLogger logger = new CommandLogger(startupArguments.htmlLog, startupArguments.textLog);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            ClevisController controller = new ClevisController(repository, logger, view);
            view.showMessage("Clevis ready. Type commands or 'quit' to exit.");
            while (controller.isRunning()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                controller.executeCommand(line);
            }
        } catch (IOException ex) {
            System.err.println("Failed to start Clevis: " + ex.getMessage());
        }
    }

    private static StartupArguments parseArguments(String[] args) {
        Path html = null;
        Path txt = null;
        for (int i = 0; i < args.length; i++) {
            String token = args[i];
            if ("-html".equalsIgnoreCase(token)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for -html");
                }
                html = Paths.get(args[++i]);
            } else if ("-txt".equalsIgnoreCase(token)) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for -txt");
                }
                txt = Paths.get(args[++i]);
            } else {
                throw new IllegalArgumentException("Unknown argument: " + token);
            }
        }
        if (html == null || txt == null) {
            throw new IllegalArgumentException("Both -html and -txt must be specified");
        }
        return new StartupArguments(html, txt);
    }

    private static void printUsage() {
        System.err.println("Usage: java hk.edu.polyu.comp.comp2021.clevis.Application -html <htmlLog> -txt <textLog>");
    }

    private static final class StartupArguments {
        private final Path htmlLog;
        private final Path textLog;

        private StartupArguments(Path htmlLog, Path textLog) {
            this.htmlLog = htmlLog;
            this.textLog = textLog;
        }
    }
}
