package CodeCheck;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Util.log("Starting up...", "%n");

        runModel();

        Util.log("Shutting down...", "%n");
    }

    private static void runModel() throws Exception {
        File baseCodeDir = new File(Util.PATH_TO_CODE);

        // Basically creating the file... // TODO: Only create the file if it's needed and not when it's empty.
        WriteObjectToFile writeToFile = new WriteObjectToFile();

        CheckDirectory checkDirectory = new CheckDirectory();
        ManyFunctions manyFunctions;
        if (!baseCodeDir.isDirectory()) {
            Util.warning(Util.PATH_TO_CODE + " is not a directory");
            if (baseCodeDir.isFile()) {
                manyFunctions = checkDirectory.checkFile(baseCodeDir);
            } else {
                Util.warning("... or a file. Please look over the config.");
                return;
            }
        } else {
            manyFunctions = checkDirectory.checkDirectory(baseCodeDir);
        }

        checkDirectory.startModel();

        for (int i = 0; i < manyFunctions.oneFunctions.size(); i++) {
            OneFunction oneFunction = manyFunctions.oneFunctions.get(i);
            List<OneFunction> laterOneFunctions = manyFunctions.oneFunctions.subList(i + 1, manyFunctions.oneFunctions.size());

            for (int j = 0; j < laterOneFunctions.size(); j++) {
                Comparison comparison = oneFunction.compare(laterOneFunctions.get(j), checkDirectory.llm);
                if (comparison.similar || comparison.equal) {
                    writeToFile.write(comparison.toString());

                    j = laterOneFunctions.size();
                }
            }
        }

        checkDirectory.stopModel();
    }
}