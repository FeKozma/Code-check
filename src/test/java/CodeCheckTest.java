import CodeCheck.CodeCheck;
import CodeCheck.Log;
import CodeCheck.Util;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeCheckTest {

    private final String userDirectory = System.getProperty("user.dir");
    private final String testConfPath = userDirectory + File.separator + "test-config.properties";
    private final String resultFolder = "test-results";
    private final String resultNamePrefix = "test-result_{nr}";
    private final String resultFileName = "test-result_0.txt";

    @Test
    public void executeTest() throws Exception {
        delete(userDirectory + File.separator + resultFolder);

        writeConf();
        CodeCheck.execute();

        assertEquals(2, readResult().split("\n").length - 1);

        emptyTestConf();
    }

    private void delete(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            Arrays.stream(dir.listFiles()).forEach(File::delete);
            dir.delete();
        } else {
            Log.warning("Could not delete '%s' directory as it doesn't exist.".formatted(resultFolder));
        }
    }

    private void writeConf() {
        List<String> conf = List.of(
                "LOGGING_LEVEL=DEBUG",
                "TEMP_FILE_ENABLED=false",
                "TEMP_FILE=debugFile.txt",
                "PATH_TO_RESULTS=" + resultFolder,
                "RESULT_NAME_PREFIX=" + resultNamePrefix,
                "RUN_WITH_LLM=false",
                "PATH_TO_CODE=src/test/resources/",
                "EXCLUDED_PATHS=[]");

        Util.write(testConfPath, "# properties used under testing - this file will be overwritten during testing", false);
        conf.forEach(line -> Util.write(testConfPath, line));
    }

    private void emptyTestConf() {
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
                Log.debug(data);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            String errorMessage = "Testing result file not found. %s".formatted(e.getMessage());
            Log.error(errorMessage);
            throw new FileNotFoundException(errorMessage + "\n" + Arrays.toString(e.getStackTrace()));
        }

        return content.toString();
    }
}
