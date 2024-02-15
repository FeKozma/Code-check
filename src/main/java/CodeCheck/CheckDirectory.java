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
            else Util.trace("Skipping file: " + file.getName());
            return new ManyFunctions(llm);
        }).reduce(new ManyFunctions(llm), ManyFunctions::new);
    }

    public ManyFunctions checkFile(File file) {
        Util.trace("ManyFunctions:checkFile -> " + file.getAbsolutePath());

        String patternMethod = "\\s*(protected|private|public)\\s+[a-zA-Z\\>\\<]*\\s([a-zA-Z_]*)\\s*\\((.*\\))";
        ManyFunctions manyFunctions = new ManyFunctions(llm);

        try {
            Scanner myReader = new Scanner(file);
            Integer nrBrackets = null;
            int linenumber = 0;
            String funcContent = "";
            while (myReader.hasNextLine()) {
                linenumber++;
                String data = myReader.nextLine();

                if (nrBrackets == null) {
                    final Pattern p = Pattern.compile(patternMethod);
                    final Matcher m = p.matcher(data);
                    while (m.find()) {
                        nrBrackets = 0;
                        manyFunctions.commitName(m.group(2));
                        manyFunctions.commitNrParameters(m.group(3).split(" ").length / 2);
                        manyFunctions.commitFile(file.getName());
                        manyFunctions.commitLine(linenumber);
                        funcContent = "";
                        Util.debug("Function: " +
                                manyFunctions.commit.name + " in " +
                                manyFunctions.commit.file + ":" +
                                manyFunctions.commit.line + " with " +
                                manyFunctions.commit.nrParams + " parameters");
                    }
                } else {
                    manyFunctions.commitContentLine(data);
                    data = data.replace(" ", "");
                    funcContent += data + "\n";
                    nrBrackets += countBrackets(data);
                    if (!(data.startsWith("//") || data.startsWith("Log") || data.startsWith("Flog") || data.isEmpty() || data.length() <= 1)) {
                        Util.log(data);
                    }
                    if (nrBrackets == 0) {
                        manyFunctions.push();
                        funcContent = "";
                        Util.debug("End of function.");
                        nrBrackets = null;
                    }
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
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
