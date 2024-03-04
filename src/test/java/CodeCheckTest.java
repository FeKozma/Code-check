import CodeCheck.CodeCheck;
import CodeCheck.Log;
import CodeCheck.Util;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodeCheckTest {

    private static final String userDirectory = System.getProperty("user.dir");
    private static final String testConfPath = userDirectory + File.separator + "test-config.properties";

    private final String resultFolder = "test-results";
    private final String resultNamePrefix = "test-result_{nr}";
    private final String resultFileName = "test-result_0.txt";


    @BeforeEach
    public void BeforeEach() {
        delete(userDirectory + File.separator + resultFolder);
    }

    @AfterAll
    public static void AfterAll() {
        emptyTestConf();
    }

    @Test
    public void executeTest() throws Exception {
        writeConf();
        CodeCheck.execute();

        assertEquals(2, readResult().split("\n").length - 1);
    }

    @Test
    public void executeMinimalTest() throws Exception {
        writeMinimalConf();
        CodeCheck.execute();

        assertEquals(2, readResult().split("\n").length - 1);
    }

    /*@Test
    public void executeEmptyTest() {
        writeEmptyConf();

        Exception e = assertThrows(NullPointerException.class, CodeCheck::execute);

        assertEquals(e.getMessage(),
                "Cannot invoke \"Object.toString()\" because the return value of \"java.util.Properties.get(Object)\" is null");
    }*/

    private void delete(String path) {
        File dir = new File(path);
        if (dir.isDirectory() && dir.getName().contains("test")) {
            Arrays.stream(dir.listFiles()).forEach(File::delete);
            dir.delete();
        } else {
            Log.log("Could not delete '%s' directory as it doesn't exist.".formatted(resultFolder));
        }
    }

    private void writeConf() {
        List<String> conf = List.of(
                "LOGGING_LEVEL=INFO",
                "PATH_TO_CODE=src/test/resources/",
                "PATH_TO_RESULTS=" + resultFolder,
                "RESULT_NAME_PREFIX=" + resultNamePrefix,
                "RESULTS_FILE_LIMIT=100",
                "EXCLUDED_PATHS=[]",
                "LLM_FILE=\"\"",
                "RUN_WITH_LLM=false",
                "LLM_THREADS=4",
                "TEMP_FILE_ENABLED=false",
                "TEMP_FILE=debugFile.txt"
        );

        Util.write(testConfPath, "# properties used under testing - this file will be overwritten during testing", false);
        conf.forEach(line -> Util.write(testConfPath, line));
    }

    private void writeMinimalConf() {
        List<String> conf = List.of(
                "PATH_TO_RESULTS=" + resultFolder, // Is optional - used for testing.
                "RESULT_NAME_PREFIX=" + resultNamePrefix, // Is optional - used for testing.
                "PATH_TO_CODE=src/test/resources/"); // Is required.

        Util.write(testConfPath, "# properties used under testing - this file will be overwritten during testing", false);
        conf.forEach(line -> Util.write(testConfPath, line));
    }

    private void writeEmptyConf() {
        List<String> conf = List.of(
                "PATH_TO_RESULTS=" + resultFolder, // Is optional - used for testing.
                "RESULT_NAME_PREFIX=" + resultNamePrefix // Is optional - used for testing.
        );

        Util.write(testConfPath, "# properties used under testing - this file will be overwritten during testing", false);
        conf.forEach(line -> Util.write(testConfPath, line));
    }

    private static void emptyTestConf() {
        Util.write(testConfPath, "# empty -- properties used under testing -- this file will be overwritten during testing", false);
    }

    private String readResult() throws FileNotFoundException {
        StringBuilder content = new StringBuilder();

        try {
            String path = userDirectory +
                    File.separator + resultFolder +
                    File.separator + resultFileName;

            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content.append("\n").append(data);
                Log.trace("readResult->data: " + data);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Testing result file not found. %s\n%s".formatted(e.getMessage(), Arrays.toString(e.getStackTrace())));
        }

        return content.toString();
    }
}
