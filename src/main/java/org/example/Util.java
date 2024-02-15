package org.example;

import java.time.LocalDateTime;

public interface Util {
    // Program variables
    LoggingLevel LOGGING_LEVEL = LoggingLevel.TRACE;
    boolean TEMP_FILE_ENABLED = false; // This is related to TEMP_FILE below.
    String TEMP_FILE = "debugFile.txt"; // If enabled, this file will be deleted and replaced every run instead of using the results files.
    String PATH_TO_RESULTS = "results"; // Where the result files are saved.
    String PATH_TO_CODE = "util"; // Path to the code to scan.

    // Styling
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";

    enum LoggingLevel {
        NONE,
        INFO,
        WARNING,
        ERROR,
        DEBUG,
        TRACE
    }

    static void log(String msg) {
        if (LoggingLevel.INFO.ordinal() <= LOGGING_LEVEL.ordinal()) log("[INFO] " + msg, LoggingLevel.INFO);
    }

    static void warning(String msg) {
        if (LoggingLevel.WARNING.ordinal() <= LOGGING_LEVEL.ordinal()) log("[WARNING] " + msg, LoggingLevel.WARNING);
    }

    static void error(String msg) {
        if (LoggingLevel.ERROR.ordinal() <= LOGGING_LEVEL.ordinal()) log("[ERROR] " + msg, LoggingLevel.ERROR);
    }

    static void debug(String msg) {
        if (LoggingLevel.DEBUG.ordinal() <= LOGGING_LEVEL.ordinal()) log("[DEBUG] " + msg, LoggingLevel.DEBUG);
    }

    static void trace(String msg) {
        if (LoggingLevel.TRACE.ordinal() <= LOGGING_LEVEL.ordinal()) log("[TRACE] " + msg, LoggingLevel.TRACE);
    }

    static void log(String msg, LoggingLevel level) {
        logReduced("[%s] ".formatted(LocalDateTime.now()) + msg + System.lineSeparator(), level);
    }

    static void log(String msg, boolean condition) {
        if (condition) log(msg, LoggingLevel.INFO);
    }

    static void log(String msg, LoggingLevel level, boolean condition) {
        if (condition) log(msg, level);
    }

    static void log(String msg, Object... formatting) {
        log(String.format(msg, formatting));
    }

    static void log(String msg, LoggingLevel level, Object... formatting) {
        log(String.format(msg, formatting), level);
    }

    static void log(String msg, LoggingLevel level, boolean condition, Object... formatting) {
        if (condition) log(msg, level, formatting);
    }

    static void logReduced(String msg, LoggingLevel level, boolean newLine) {
        String colorLevel = switch (level) {
            case NONE -> "";
            case INFO -> ANSI_RESET;
            case DEBUG -> ANSI_GREEN;
            case TRACE -> ANSI_BLUE;
            case WARNING -> ANSI_YELLOW;
            case ERROR -> ANSI_RED;
        };

        if (newLine) System.out.println(colorLevel + msg + ANSI_RESET);
        else System.out.print(colorLevel + msg + ANSI_RESET);
    }

    static void logReduced(String msg, LoggingLevel level) {
        logReduced(msg, level, false);
    }

    static void logReduced(String msg) {
        logReduced(msg, LoggingLevel.INFO, false);
    }

    static void logReduced(String msg, Object... formatting) {
        logReduced(String.format(msg, formatting), LoggingLevel.INFO, false);
    }
}
