package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteObjectToFile {

    File file;

    public WriteObjectToFile() throws Exception {

        // Delete the temp result file if enabled and if it exists.
        if (Util.TEMP_FILE_ENABLED) {
            File tempFile = new File(Util.PATH_TO_RESULTS + "/" + Util.TEMP_FILE);
            if (tempFile.exists()) tempFile.delete();
            Util.log("Debug mode enabled, deleting the temporary file %s before continuing... ".formatted(Util.PATH_TO_RESULTS + "/" + Util.TEMP_FILE));
        }

        // Create results directory if it doesn't exist.
        if (!(new File(Util.PATH_TO_RESULTS).exists())) new File(Util.PATH_TO_RESULTS).mkdir();

        // Look for the next file to write towards.
        int maxResultsFiles = 100;
        for (int i = 0; i < maxResultsFiles; i++) {
            String fileName = Util.TEMP_FILE_ENABLED ? Util.TEMP_FILE : "result_" + i + ".txt";
            if (createFile(fileName, i == 0)) break;
            if (i == maxResultsFiles - 1) {
                String errorMessage = "Warning! The maximum amount of result of %d files has been reached! Cannot continue execution.".formatted(maxResultsFiles);
                Util.error(errorMessage);
                throw new Exception(errorMessage);
            }
        }
    }

    private boolean createFile(String fileName, boolean isFirstTry) {
        try {
            file = new File(Util.PATH_TO_RESULTS + "/" + fileName);

            if (file.createNewFile()) {
                Util.log(String.format("File created: %s", file.getPath()));
                return true;
            } else {
                if (isFirstTry)
                    Util.logReduced("The file %s already exists, trying to create the next id... ", file.getPath());
                else
                    Util.logReduced("%s ... ", file.getPath());

                return false;
            }
        } catch (IOException e) {
            Util.error("An error occurred while writing the file %s.".formatted(file.getPath()));
            e.printStackTrace();
            return true; // An error has occurred -- stop the loop.
        }
    }

    public void write(String obj) {
        try {
            FileWriter myWriter = new FileWriter(file.getName(), true);
            myWriter.write(obj.replace("\n", "\\n") + "\n");
            myWriter.close();
        } catch (IOException e) {
            Util.error("An error occurred while writing to the file %s.".formatted(file.getPath()));
            e.printStackTrace();
        }
    }
}
