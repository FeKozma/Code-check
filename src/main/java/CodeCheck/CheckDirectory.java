package CodeCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckDirectory {

    LLM llm = new LLM();

    private final List<Pattern> EXCLUDED_PATHS = ConfigInterface
            .conf
            .getList("EXCLUDED_PATHS")
            .orElse(Collections.emptyList())
            .stream()
            .filter(Predicate.not(String::isEmpty))
            .map(path -> "((?i)" + path.trim() + "(?-i))")
            .map(Pattern::compile)
            .toList();

    public void startModel() {
        llm.initModel();
    }

    public void stopModel() {
        llm.cleanModel();
    }

    public ManyFunctions checkDirectory(File dir) {

        return Arrays.stream(dir.listFiles()).map((file) -> {
            if (file.isDirectory() && checkExcluded(file)) return checkDirectory(file);
            else if (file.getName().endsWith(".java") && checkExcluded(file)) return checkFile(file);
            else Log.trace("Skipping file: " + file.getName());
            return new ManyFunctions(llm);
        }).reduce(new ManyFunctions(llm), ManyFunctions::new);
    }

    public boolean checkExcluded(File file) {
        if (EXCLUDED_PATHS.isEmpty()) return true;

        for (Pattern pattern : EXCLUDED_PATHS) {
            final Matcher m = pattern.matcher(file.getPath());

            if (m.find()) {
                Log.log("File excluded: %s", file.getPath());
                return false;
            }
        }

        return true;
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

                        Log.debug("Accessing file at the path: " + file.getAbsolutePath());
                        Log.trace("Function: " +
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
