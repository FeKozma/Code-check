package CodeCheck;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        Log.log("Starting up at: %s.".formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        Log.log("Running on %s.".formatted(System.getProperty("os.name")));

        CodeCheck.execute();

        Log.log("Shutting down at: %s.".formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))));
        Log.log("Total duration: %s".formatted(Util.getFormattedDurationTime(startTime)));
    }
}