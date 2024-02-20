package CodeCheck;

import java.time.LocalDateTime;

public interface Log {

    // Styling
    enum Color {
        NONE { @Override public String toString() { return ""; } },
        RESET { @Override public String toString() { return WHITE.toString(); } },
        WHITE { @Override public String toString() { return "\u001B[0m"; } },
        RED { @Override public String toString() { return "\u001B[31m"; } },
        GREEN { @Override public String toString() { return "\u001B[32m"; } },
        YELLOW { @Override public String toString() { return "\u001B[033m"; } },
        BLUE { @Override public String toString() { return "\u001B[34m"; } },
        PURPLE { @Override public String toString() { return "\u001B[35m"; } },
        BLACK { @Override public String toString() { return "\u001B[36m"; } },
        CYAN { @Override public String toString() { return "\u001B[37m"; } };
    }

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

    static void log(String msg, ConfigInterface.Config.LoggingLevel lvl) {
        ConfigInterface.Config.LoggingLevel logLvl = ConfigInterface.conf.getLogLvl();
        if (logLvl.logOn(lvl)) {
            logReduced("%s [%s] %s".formatted(
                    LocalDateTime.now(),
                    lvl.name(),
                    msg + System.lineSeparator()),
                    lvl);
        }
    }

    static void log(String msg, Object... formatting) {
        log(String.format(msg, formatting));
    }

    static void log(String msg, ConfigInterface.Config.LoggingLevel level, Object... formatting) {
        log(String.format(msg, formatting), level);
    }

    static void logReduced(String msg, ConfigInterface.Config.LoggingLevel level, boolean newLine) {
        Color colorLevel = switch (level) {
            case NONE, N, OFF, O -> Color.NONE; // No change in color.
            case ERROR, ERR, E -> Color.RED;
            case WARNING, WARN, W -> Color.YELLOW;
            case INFO, I -> Color.WHITE;
            case DEBUG, D -> Color.GREEN;
            case TRACE, T -> Color.BLUE;
        };

        if (newLine) System.out.println(colorLevel + msg + Color.RESET);
        else System.out.print(colorLevel + msg + Color.RESET);
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

    static void logColor(String msg, Color color) {
        System.out.printf(
                "%s%s [%s] %s%n",
                color,
                LocalDateTime.now(),
                ConfigInterface.Config.LoggingLevel.INFO,
                msg
        );
    }
}
