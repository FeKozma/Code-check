package CodeCheck;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteObjectToFile {

    private final String PATH_TO_RESULTS = ConfigInterface.conf.getString("PATH_TO_RESULTS");
    private final String RESULT_NAME_PREFIX = ConfigInterface.conf.getString("RESULT_NAME_PREFIX");
    private final boolean TEMP_FILE_ENABLED = ConfigInterface.conf.getBoolean("TEMP_FILE_ENABLED");
    private final String TEMP_FILE =  ConfigInterface.conf.getString("TEMP_FILE");
    File file;

    public WriteObjectToFile() throws Exception {

        // Delete the temp result file if enabled and if it exists.
        if (TEMP_FILE_ENABLED) {
            File tempFile = new File(PATH_TO_RESULTS + "/" + TEMP_FILE);
            if (tempFile.exists()) tempFile.delete();
            Log.log("Debug mode enabled, deleting the temporary file %s before continuing... ".formatted(PATH_TO_RESULTS + "/" + TEMP_FILE));
        }

        // Create results directory if it doesn't exist.
        if (!(new File(PATH_TO_RESULTS).exists())) {
            new File(PATH_TO_RESULTS).mkdir();
            Log.log("Created directory %s...", PATH_TO_RESULTS);
        }

        // Look for the next file to write towards.
        int maxResultsFiles = 100;
        for (int i = 0; i < maxResultsFiles; i++) {

            String prefix = RESULT_NAME_PREFIX.isEmpty()
                    ? "result_" + i // If nothing is set, use the default value.
                    : RESULT_NAME_PREFIX.contains("{nr}") // If {nr} exist, replace all with the counter value.
                    ? RESULT_NAME_PREFIX.replaceAll("\\{nr}", String.valueOf(i))
                    : RESULT_NAME_PREFIX + i; // If {nr} doesn't exist, add the counter to the end of prefix.

            String fileName = TEMP_FILE_ENABLED ? TEMP_FILE : prefix + ".txt";

            if (createFile(fileName, i == 0)) break;

            if (i == maxResultsFiles - 1) {
                String errorMessage = "Warning! The maximum amount of result of %d files has been reached! Cannot continue execution.".formatted(maxResultsFiles);
                Log.error(errorMessage);
                throw new Exception(errorMessage);
            }
        }
    }

    private boolean createFile(String fileName, boolean isFirstTry) {
        file = Util.createFile(PATH_TO_RESULTS + "/" + fileName, isFirstTry);
        return file != null;
    }

    public void write(String obj) {
        Util.write(PATH_TO_RESULTS + "/" + file.getName(), obj);
    }
}
