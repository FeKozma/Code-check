package CodeCheck;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public interface ConfigInterface {
    Config conf = new Config();

    class Config {
        enum LoggingLevel {
            NONE (0),
            ERROR(1),
            ERR(1),
            E(1),
            WARNING(2),
            WARN(2),
            W(2),
            DEBUG(3),
            D(3),
            INFO(4),
            I(4),
            TRACE(5),
            T(5);

            private int lvl;
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


            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("(.*Code-check/).*", "$1");

            String appConfigPath = rootPath + "config.properties";
            if (new File(rootPath + "local-config.properties").exists()) {
                appConfigPath = rootPath + "local-config.properties";

            } else if (new File(rootPath + "test-config.properties").exists()) {
                appConfigPath = rootPath + "test-config.properties";
            }

            try {
                appProps.load(new FileInputStream(appConfigPath));
            } catch (Exception e) {
                Util.error(e.getMessage());
            }
        }

        public String getString(String key) {
            return appProps.get(key).toString();
        }

        public Boolean getBoolean(String key) {
            return  appProps.get(key).toString().equalsIgnoreCase("true");
        }

        public LoggingLevel getLogLvl() {
            return LoggingLevel.valueOf(getString("LOGGING_LEVEL"));
        }
    }
}
