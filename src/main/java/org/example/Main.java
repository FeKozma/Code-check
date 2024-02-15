package org.example;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Util.log("[%s] Starting up...%n", LocalDateTime.now());

        File baseCodeDir = new File(Util.PATH_TO_CODE);
        if (!baseCodeDir.isDirectory()) {
            String errorMessage = "The variable PATH is not a directory, please change the value in Util.java. Current path: %s}".formatted(Util.PATH_TO_CODE);
            Util.error(errorMessage);
            throw new NullPointerException(errorMessage);
        }

        WriteObjectToFile writeToFile = new WriteObjectToFile();

        CheckDirectory checkDirectory = new CheckDirectory();
        ManyFunctions manyFunctions = checkDirectory.checkDirectory(baseCodeDir);

        checkDirectory.startModel();
        for (int i = 0; i < manyFunctions.oneFunctions.size(); i++) {
            OneFunction oneFunction = manyFunctions.oneFunctions.get(i);
            List<OneFunction> laterOneFunctions = manyFunctions.oneFunctions.subList(i+1, manyFunctions.oneFunctions.size());

            for (int j = 0; j < laterOneFunctions.size(); j++) {
                Comparison comparison = oneFunction.compare(laterOneFunctions.get(j), checkDirectory.llm);
                if (comparison.similar || comparison.equal) {
                    writeToFile.write(comparison.toString());

                    j = laterOneFunctions.size();
                }
            }
        }
        checkDirectory.stopModel();

        Util.log("[%s] Closing down...%n", LocalDateTime.now());
    }
}