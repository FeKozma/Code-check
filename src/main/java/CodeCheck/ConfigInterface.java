package CodeCheck;

import java.io.*;
import java.util.*;

public interface ConfigInterface {
    Config conf = new Config();

    class Config {
        enum LoggingLevel {
            NONE(0), N(NONE.lvl), OFF(NONE.lvl), O(OFF.lvl),
            ERROR(1), ERR(ERROR.lvl), E(ERROR.lvl),
            WARNING(2), WARN(WARNING.lvl), W(WARNING.lvl),
            INFO(3), I(INFO.lvl),
            DEBUG(4), D(DEBUG.lvl),
            TRACE(5), T(TRACE.lvl);

            public boolean logOn(LoggingLevel lvl) {
                return lvl.lvl <= this.lvl;
            }

            private final int lvl;

            LoggingLevel(int lvl) {
                this.lvl = lvl;
            }
        }

        private Properties appProps;

        public Config() {}

        public void loadConfig() throws IOException {
            this.appProps = new Properties();

            final String rootDirPath = System.getProperty("user.dir") + File.separator;
            final File default_conf = new File(rootDirPath + "config.properties");
            final File test_conf = new File(rootDirPath + "test-config.properties");
            final File local_conf = new File(rootDirPath + "local-config.properties");

            File appConfigPathLocalSquish = null;

            if (test_conf.isFile() && countLines(test_conf) > 1) {
                appConfigPathLocalSquish = test_conf;
            } else if (local_conf.isFile() && countLines(local_conf) >= 1) {
                appConfigPathLocalSquish = local_conf;
            }

            try {
                Log.log("Default configuration file `%s` found, loading properties.".formatted(default_conf.getName()));
                appProps.load(new FileInputStream(default_conf));

                if (appConfigPathLocalSquish != null) {
                    Log.log("Found `%s`, loading extra configuration properties.".formatted(appConfigPathLocalSquish.getName()), Log.Color.PURPLE);
                    appProps.load(new FileInputStream(appConfigPathLocalSquish));
                }
            } catch (Exception e) {
                throw new NullPointerException("Could not find any matching expression for root path \"%s\".\n%s"
                        .formatted(rootDirPath, e.getMessage()));
            }
        }

        public String getProperty(String key) {
            if (appProps == null) {
                Log.warning("'%s' not initialized. The key '%s' was not loaded".formatted(appProps.getClass().getSimpleName(), key));
                throw new RuntimeException("'appProps' is null - cannot continue. Configuration not initialized.");
            }

            try {
                return appProps.get(key).toString().strip();
            } catch (NullPointerException e) {
                throw new NullPointerException(e.getMessage());
            }
        }

        public Optional<String> getString(String key) {
            try {
                return Optional.of(getProperty(key));
            } catch (NullPointerException e) {
                return Optional.empty();
            }
        }

        public Optional<Boolean> getBoolean(String key) {
            try {
                return Optional.of(getProperty(key).equalsIgnoreCase("true"));
            } catch (NullPointerException e) {
                return Optional.empty();
            }
        }

        public Optional<Integer> getInteger(String key) {
            try {
                return Optional.of(Integer.parseInt(getProperty(key)));
            } catch (NumberFormatException | NullPointerException e) {
                return Optional.empty();
            }
        }

        public Optional<List<String>> getList(String key) {
            try {
                String value = getProperty(key);

                if (value == null)
                    return Optional.of(Collections.emptyList());

                return Optional.of(
                        Arrays
                        .stream(value.substring(1, value.length() - 1).split(","))
                        .map(String::strip)
                        .toList());
            } catch (NullPointerException e) {
                return Optional.empty();
            }
        }

        public LoggingLevel getLogLvl() {
            String loggingLevel = getString("LOGGING_LEVEL")
                    .orElse("INFO");

            try {
                return LoggingLevel.valueOf(loggingLevel.toUpperCase());
            } catch (IllegalArgumentException e) {
                Log.error(getLogLvl().name() + "::LoggingLevel got an error. %s.".formatted(e.getMessage()));
                return LoggingLevel.INFO;
            }
        }

        private long countLines(File file) throws IOException {
            return new BufferedReader(new FileReader(file)).lines().count();
        }
    }
}
