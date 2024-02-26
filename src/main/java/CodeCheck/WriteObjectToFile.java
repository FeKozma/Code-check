package CodeCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WriteObjectToFile {

    private static final boolean TEMP_FILE_ENABLED = ConfigInterface.conf.getBoolean("TEMP_FILE_ENABLED")
            .orElse(false);
    private static final String TEMP_FILE = Util.checkIfHomePath(ConfigInterface.conf.getString("TEMP_FILE")
            .orElse("debugFile.txt"));
    private static final String PATH_TO_RESULTS = Util.checkIfHomePath(ConfigInterface.conf.getString("PATH_TO_RESULTS")
            .orElse("results"));
    private static final String RESULT_NAME_PREFIX = ConfigInterface.conf.getString("RESULT_NAME_PREFIX")
            .orElse("result_{nr}");
    private static final int RESULTS_FILE_LIMIT = ConfigInterface.conf.getInteger("RESULTS_FILE_LIMIT")
            .orElse(100);
    private File file;

    public WriteObjectToFile() throws Exception {

        // Delete the temp result file if enabled and if it exists.
        if (TEMP_FILE_ENABLED) {
            File tempFile = new File(PATH_TO_RESULTS + File.separator + TEMP_FILE);

            if (tempFile.isFile()) {
                try {
                    tempFile.delete();
                    Log.log("Debug mode enabled - deleting the temporary results file '%s' before continuing... ".formatted(tempFile.getPath()));
                } catch (Exception e) {
                    throw new IOException("Debug mode enabled - could not delete the temporary results file '%s'!".formatted(tempFile.getPath()), e.getCause());
                }
            } else if (tempFile.isDirectory()) {
                throw new FileNotFoundException("Debug mode enabled - The temporary file '%s' is a directory. Cannot continue!".formatted(tempFile.getPath()));
            }
        }

        // Create results directory if it doesn't exist.
        File resultsDir = new File(PATH_TO_RESULTS);
        if (resultsDir.isDirectory()) {
            Log.log("The results directory `%s` already exists, no need to create it.".formatted(resultsDir.getName()));
        } else {
            if (resultsDir.mkdirs()) {
                Log.log("Created directory %s...".formatted(resultsDir.getAbsolutePath()));
            } else {
                throw new Exception("Could not create directory %s.".formatted(resultsDir.getAbsolutePath()));
            }
        }

        // Look for the next file to write towards.
        for (int i = 0; i < RESULTS_FILE_LIMIT; i++) {

            String prefix = RESULT_NAME_PREFIX.isEmpty() // Check if it's empty - in case it would be `""`.
                    ? "result_%d.txt".formatted(i) // Nothing is set, so use the default value.
                    : RESULT_NAME_PREFIX.contains("{nr}") // If `{nr}` exists, then replace `{nr}`.
                    ? RESULT_NAME_PREFIX.replace("{nr}", String.valueOf(i)) // Replace {nr} with the current counter value.
                    : RESULT_NAME_PREFIX + i; // If `{nr}` doesn't exist, add the incremental counter to the end of the prefix value.

            String fileName = TEMP_FILE_ENABLED ? TEMP_FILE : prefix + ".txt";

            if (createFile(fileName, i == 0)) break;

            if (i >= RESULTS_FILE_LIMIT - 1) {
                throw new Exception("Warning! The maximum amount of result of %d files has been reached! Cannot continue execution. Please increase the maximum limit, use another filename or path, or delete existing result files.".formatted(RESULTS_FILE_LIMIT));
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
