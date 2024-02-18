package CodeCheck;

import java.time.LocalDateTime;

public interface Log {

    // Styling
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";


    static void log(String msg) {
        log(msg, ConfigInterface.Config.LoggingLevel.INFO);
    }

    static void warning(String msg) {
        log(msg, ConfigInterface.Config.LoggingLevel.WARNING);
    }

    static void error(String msg) {
        log(msg, ConfigInterface.Config.LoggingLevel.ERROR);
    }

    static void debug(String msg) {
        log(msg, ConfigInterface.Config.LoggingLevel.DEBUG);
    }

    static void trace(String msg) {
        log(msg, ConfigInterface.Config.LoggingLevel.TRACE);
    }

    private static void log(String msg, ConfigInterface.Config.LoggingLevel lvl) {
        ConfigInterface.Config.LoggingLevel logLvl = ConfigInterface.conf.getLogLvl();
        if (logLvl.logOn(lvl)) {
            logReduced("[%s] ".formatted(LocalDateTime.now()) + "[" + lvl.name() + "] " + msg + System.lineSeparator(), lvl);
        }
    }

    static void log(String msg, Object... formatting) {
        log(String.format(msg, formatting));
    }

    static void log(String msg, ConfigInterface.Config.LoggingLevel level, Object... formatting) {
        log(String.format(msg, formatting), level);
    }

    static void logReduced(String msg, ConfigInterface.Config.LoggingLevel level, boolean newLine) {
        String colorLevel = switch (level) {
            case NONE -> "";
            case INFO, I -> ANSI_RESET;
            case DEBUG, D -> ANSI_GREEN;
            case TRACE, T -> ANSI_BLUE;
            case WARNING, WARN, W -> ANSI_YELLOW;
            case ERROR, ERR, E -> ANSI_RED;
        };

        if (newLine) System.out.println(colorLevel + msg + ANSI_RESET);
        else System.out.print(colorLevel + msg + ANSI_RESET);
    }

    static void logReduced(String msg, ConfigInterface.Config.LoggingLevel level) {
        logReduced(msg, level, false);
    }

    static void logReduced(String msg) {
        logReduced(msg, ConfigInterface.Config.LoggingLevel.INFO, false);
    }

    static void logReduced(String msg, Object... formatting) {
        logReduced(String.format(msg, formatting), ConfigInterface.Config.LoggingLevel.INFO, false);
    }

    /**
     * Calculate the duration from the start time and the current time.
     *
     * @param startTime Input the start time.
     * @return An integer array of time the duration in HH:MM:SS:MS. 0 = h, 1 = m, 2 = s, 3 = ms
     */
    static int[] getCalcDurationTime(long startTime) {
        long millis = (System.currentTimeMillis() - startTime);
        int seconds = (int) millis / 1000;
        int minutes = (seconds % 3600) / 60;
        int hours = seconds / 3600;

        return new int[]{hours, minutes, seconds % 60, (int) millis % 1000};
    }
}
