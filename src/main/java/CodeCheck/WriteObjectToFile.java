package CodeCheck;

import java.io.File;
import java.io.FileNotFoundException;

public class WriteObjectToFile {

    private final String PATH_TO_RESULTS = Util.checkIfHomePath(ConfigInterface.conf.getString("PATH_TO_RESULTS"));
    private final String RESULT_NAME_PREFIX = ConfigInterface.conf.getString("RESULT_NAME_PREFIX");
    private final boolean TEMP_FILE_ENABLED = ConfigInterface.conf.getBoolean("TEMP_FILE_ENABLED");
    private final String TEMP_FILE =  Util.checkIfHomePath(ConfigInterface.conf.getString("TEMP_FILE"));
    File file;

    public WriteObjectToFile() throws Exception {

        // Delete the temp result file if enabled and if it exists.
        if (TEMP_FILE_ENABLED) {
            File tempFile = new File(PATH_TO_RESULTS + File.separator + TEMP_FILE);
            if (tempFile.isFile()) {
                tempFile.delete();
                Log.log("Debug mode enabled, deleting the temporary file %s before continuing... ".formatted(PATH_TO_RESULTS + File.separator + TEMP_FILE));
            }
            else if (tempFile.isDirectory()) {
                String errorMsg = "Debug mode enabled - The temporary file '%s' is a directory. Cannot continue!";
                Log.error(errorMsg);
                throw new FileNotFoundException(errorMsg);
            }
        }

        // Create results directory if it doesn't exist.
        File file = new File(PATH_TO_RESULTS);
        if (!file.isDirectory()) {
            if (file.mkdirs()) {
                Log.log("Created directory %s...".formatted(file.getAbsolutePath()));
            } else {
                Log.error("Could not create directory %s.".formatted(file.getAbsolutePath()));
                throw new Exception("Could not create directory %s.".formatted(file.getAbsolutePath()));
            }
        } else {
            Log.log("The results directory `%s` already exists, no need to create it.".formatted(file.getName()));
        }

        // Look for the next file to write towards.
        final int maxResultsFiles = 100;

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
        file = Util.createFile(PATH_TO_RESULTS + File.separator + fileName, isFirstTry);
        return file != null;
    }

    public void write(String obj) {
        Util.write(PATH_TO_RESULTS + File.separator + file.getName(), obj);
    }
}
