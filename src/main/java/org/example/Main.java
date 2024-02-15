package org.example;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {


        File baseDir = new File(Util.PATH);
        if (!baseDir.isDirectory()) {
            System.out.println(Util.ANSI_RED + "PATH is not a directory, please change the value in Util.java");
            throw new Exception("PATH is not a directory, please change the value in Util.java");
        }

        WriteObjectToFile writeToFile = new WriteObjectToFile();


        CheckDirectory checkDirectory = new CheckDirectory();
        ManyFunctions manyFunctions = checkDirectory.checkDirectory(baseDir);

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

    }
}