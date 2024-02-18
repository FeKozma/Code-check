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
                    Log.logReduced("The file %s already exist, trying to create the next id... ", file.getPath());
                else
                    Log.logReduced("%s ... ", file.getPath());

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
}
