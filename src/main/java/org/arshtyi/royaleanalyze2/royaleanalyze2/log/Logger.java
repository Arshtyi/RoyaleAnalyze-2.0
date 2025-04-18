package org.arshtyi.royaleanalyze2.royaleanalyze2.log;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to log messages to a file.
 * It provides methods to log messages with different severity levels (INFO,
 * ERROR, etc.), and includes the class name, method name, and current time in
 * the log message.
 * It also handles exceptions that may occur during logging.
 * It is designed to be used as a singleton class, so it cannot be instantiated.
 */
public class Logger {
    /**
     * This constructor is private to prevent instantiation of the Logger class.
     */
    private Logger() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ANSI color codes for console output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    /**
     * This method logs a message to a file with the specified severity level.
     * It includes the class name, method name, and current time in the log message.
     * It also handles exceptions that may occur during logging.
     * 
     * @param level
     *            The severity level of the log message (e.g., INFO, ERROR).
     * 
     * @param message
     *            The log message to be written to the file.
     * 
     * @param sourceClass
     *            The class from which the log message is generated.
     * 
     * @param filePath
     *            The path to the log file.
     * 
     * @return void
     * 
     * @throws IOException
     *             If an I/O error occurs while writing to the log file.
     */
    public static void log(String level, String message, Class<?> sourceClass, String filePath) {
        /**
         * Get the current time and format it as "yyyy-MM-dd -- HH:mm:ss.SSS".
         */
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd -- HH:mm:ss.SSS"));
        /**
         * Format the log message to include the timestamp, severity level, class name,
         * method name, package name, and the log message itself.
         */
        String logMessage = String.format("%s  %s  -- [%s.%s@%s] : %s", timestamp, level,
                sourceClass.getSimpleName(),
                Thread.currentThread().getStackTrace()[2].getMethodName(),
                sourceClass.getPackage().getName(),
                message);
        // Get the appropriate color based on log level
        String color;
        switch (level.toUpperCase()) {
            case "INFO":
                color = ANSI_GREEN;
                break;
            case "ERROR":
                color = ANSI_RED;
                break;
            case "WARNING":
                color = ANSI_YELLOW;
                break;
            case "DEBUG":
                color = ANSI_CYAN;
                break;
            default:
                color = ANSI_RESET;
        }

        // Print to console if filePath is null or empty
        if (filePath == null || filePath.isEmpty()) {
            System.out.println(color + logMessage + ANSI_RESET);
            return;
        }
        // Write to file
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(logMessage + System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}
