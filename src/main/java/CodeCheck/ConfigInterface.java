package CodeCheck;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public interface ConfigInterface {
    Config conf = new Config();

    class Config {
        enum LoggingLevel {
            NONE(0),
            N(NONE.lvl),
            OFF(NONE.lvl),
            O(OFF.lvl),
            ERROR(1),
            ERR(ERROR.lvl),
            E(ERROR.lvl),
            WARNING(2),
            WARN(WARNING.lvl),
            W(WARNING.lvl),
            INFO(3),
            I(INFO.lvl),
            DEBUG(4),
            D(DEBUG.lvl),
            TRACE(5),
            T(TRACE.lvl);

            private final int lvl;

            public boolean logOn(LoggingLevel lvl) {
                return lvl.lvl <= this.lvl;
            }

            LoggingLevel(int i) {
                lvl = i;
            }
        }

        Properties appProps;

        public Config() {}

        public void loadConfig() throws IOException {
            this.appProps = new Properties();

            String rootDirPath = FileSystems.getDefault().getPath("").toAbsolutePath().toString() + File.separator;
            String appConfigPath = rootDirPath + "config.properties";
            File test_conf = new File(rootDirPath + "test-config.properties");

            if (test_conf.isFile() && countLines(test_conf) > 1) {
                appConfigPath = rootDirPath + "test-config.properties";

            } else if (new File(rootDirPath + "local-config.properties").isFile()) {
                appConfigPath = rootDirPath + "local-config.properties";
            }

            try {
                appProps.load(new FileInputStream(appConfigPath));
            } catch (Exception e) {
                throw new NullPointerException("Could not find any matching expression for root path \"%s\".\n%s"
                        .formatted(rootDirPath, e.getMessage()));
            }
        }

        public String getString(String key) {
            if (appProps == null) return null;
            return appProps.get(key).toString();
        }

        public Boolean getBoolean(String key) {
            return appProps.get(key).toString().equalsIgnoreCase("true");
        }

        public List<String> getList(String key) {
            String value = getString(key);
            if (value == null) return Collections.emptyList();
            return Arrays
                    .stream(value.substring(1, value.length() - 1).split(","))
                    .map(String::strip)
                    .toList();
        }

        public LoggingLevel getLogLvl() {
            String loggingLevel = getString("LOGGING_LEVEL");
            if (loggingLevel == null) loggingLevel = "DEBUG";
            return LoggingLevel.valueOf(loggingLevel);
        }

        private int countLines(File file) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int lines = 0;
            while (reader.readLine() != null) lines++;
            reader.close();
            return lines;
        }
    }
}
