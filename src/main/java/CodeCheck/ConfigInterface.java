package CodeCheck;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public interface ConfigInterface {
    Config conf = new Config();

    class Config {
        enum LoggingLevel {
            NONE(0),
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

            public Boolean logOn(LoggingLevel lvl) {
                return lvl.lvl <= this.lvl;
            }

            LoggingLevel(int i) {
                lvl = i;
            }
        }

        Properties appProps;

        public Config() {
            this.appProps = new Properties();

            String rootDirRegex = "(.*Code-check/).*";
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll(rootDirRegex, "$1");

            String appConfigPath = rootPath + "config.properties";
            if (new File(rootPath + "local-config.properties").exists()) {
                appConfigPath = rootPath + "local-config.properties";

            } else if (new File(rootPath + "test-config.properties").exists()) {
                appConfigPath = rootPath + "test-config.properties";
            }

            try {
                appProps.load(new FileInputStream(appConfigPath));
            } catch (Exception e) {
                throw new NullPointerException("Could not find any matching expression for root path \"%s\".\n%s"
                        .formatted(rootDirRegex, e.getMessage()));
            }
        }

        public String getString(String key) {
            return appProps.get(key).toString();
        }

        public Boolean getBoolean(String key) {
            return  appProps.get(key).toString().equalsIgnoreCase("true");
        }

        public List<String> getList(String key) {
            String sList = getString(key);
            sList = sList
                    .substring(1)
                    .substring(0, sList.length() - 2);
            return Arrays
                    .stream(sList.split(","))
                    .map(String::strip)
                    .toList();
        }

        public LoggingLevel getLogLvl() {
            return LoggingLevel.valueOf(getString("LOGGING_LEVEL"));
        }
    }
}
