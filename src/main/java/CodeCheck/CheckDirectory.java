package CodeCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckDirectory {

    LLM llm = new LLM();

    public void startModel() {
        llm.initModel();
    }

    public void stopModel() throws Exception {
        llm.cleanModel();
    }

    public ManyFunctions checkDirectory(File dir) {

        return Arrays.stream(dir.listFiles()).map((file) -> {
            if (file.isDirectory()) return checkDirectory(file);
            else if (file.getName().endsWith(".java")) return checkFile(file);
            else Log.trace("Skipping file: " + file.getName());
            return new ManyFunctions(llm);
        }).reduce(new ManyFunctions(llm), ManyFunctions::new);
    }

    public ManyFunctions checkFile(File file) {
        Log.trace("ManyFunctions:checkFile -> " + file.getAbsolutePath());

        String patternMethod = "\\s*(protected|private|public)\\s+[a-zA-Z\\>\\<]*\\s*([a-zA-Z0-9_]*)\\s*\\((.*\\))";
        ManyFunctions manyFunctions = new ManyFunctions(llm);

        try {
            Scanner myReader = new Scanner(file);
            Integer nrBrackets = null;
            int lineNumber = 0;

            while (myReader.hasNextLine()) {
                lineNumber++;
                String data = myReader.nextLine();

                if (nrBrackets == null) {
                    final Pattern p = Pattern.compile(patternMethod);
                    final Matcher m = p.matcher(data);

                    while (m.find()) {
                        nrBrackets = countBrackets(data);
                        manyFunctions.commitName(m.group(2));
                        manyFunctions.commitNrParameters(m.group(3).split(" ").length / 2);
                        manyFunctions.commitFile(file.getName());
                        manyFunctions.commitLine(lineNumber);

                        Log.debug("Function: " +
                                manyFunctions.commit.name + " in " +
                                manyFunctions.commit.file + ":" +
                                manyFunctions.commit.line + " with " +
                                manyFunctions.commit.nrParams + " parameters");
                    }
                } else {
                    manyFunctions.commitContentLine(data);
                    data = data.replace(" ", "");

                    nrBrackets += countBrackets(data);
                    if (!(data.startsWith("//") || data.startsWith("Log") || data.startsWith("Flog") || data.isEmpty() || data.length() <= 1)) {
                        Log.trace(data);
                    }
                    if (nrBrackets == 0) {
                        manyFunctions.push();
                        Log.trace("End of function.");
                        nrBrackets = null;
                    }
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            Log.error("An error occurred.");
            e.printStackTrace();
        }
        return manyFunctions;
    }

    private static int countBrackets(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '{') count++;
            if (str.charAt(i) == '}') count--;
        }
        return count;
    }

}
