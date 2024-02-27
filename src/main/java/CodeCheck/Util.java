package CodeCheck;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface Util {

    static File createFile(String filePath, boolean isFirstTry) {
        File file = new File(filePath);

        try {
            if (file.createNewFile()) {
                Log.log(String.format("File created: %s", file.getPath()));
                return file;
            } else {
                if (isFirstTry)
                    Log.log("The file %s already exist, trying to create the next id... ", file.getPath());
                else
                    Log.trace("Trying %s... ".formatted(file.getName()));

                return null;
            }
        } catch (IOException e) {
            Log.error("An error occurred while trying to create the file %s.".formatted(file.getPath()));
            e.printStackTrace();
            return null; // An error has occurred -- stop the loop by returning null.
        }
    }

    static void write(String filePath, String msg) {
        write(filePath, msg, true);
    }

    static void write(String filePath, String msg, boolean append) {
        try {
            FileWriter myWriter = new FileWriter(filePath, append);
            myWriter.write(msg.replace("\n", "\\n") + "\n");
            myWriter.close();
        } catch (IOException e) {
            Log.error("An error occurred while writing to the file %s.".formatted(filePath));
            e.printStackTrace();
        }
    }

    /**
     * Calculate the duration from the start time and the current time.
     *
     * @param startTime Input the start time.
     * @return An integer array of time the duration in HH:MM:SS:MS. 0 = h, 1 = m, 2 = s, 3 = ms
     */
    static int[] getPreparedTime(long startTime, long endTime) {
        long millis = (endTime - startTime);
        int seconds = (int) millis / 1000;
        int minutes = (seconds % 3600) / 60;
        int hours = seconds / 3600;

        return new int[] {hours, minutes, seconds % 60, (int) millis % 1000};
    }

    static int[] getPreparedDurationTime(long startTime) {
        return getPreparedTime(startTime, System.currentTimeMillis());
    }

    static String getFormattedDurationTime(long startTime, long endTime) {
        int[] t = getPreparedTime(startTime, endTime);
        return "%02d hours, %02d minutes, %02d seconds and %02d milliseconds"
                .formatted(t[0], t[1], t[2], t[3]);
    }

    static String getFormattedDurationTime(long startTime) {
        return getFormattedDurationTime(startTime, System.currentTimeMillis());
    }

    static String checkIfHomePath(String path) {
        if (path.startsWith("~")) {
            return System.getProperty("user.home") + path.substring(1);
        } else {
            return path;
        }
    }
}
