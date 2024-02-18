import CodeCheck.CodeCheck;
import CodeCheck.Log;
import CodeCheck.Util;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.List;

public class CodeCheckTest {

    private String testConfPath = FileSystems.getDefault()
            .getPath("").toAbsolutePath() + "\\test-config.properties";

    private String resultFolder = "test-results";
    @Test
    public void executeTest() throws Exception {
        delete(testConfPath);
        delete(FileSystems.getDefault()
                .getPath("").toAbsolutePath() + "\\" +resultFolder);
        Util.createFile(testConfPath, true);
        writeConf();
        CodeCheck.execute();
        delete(testConfPath);
    }

    private void delete(String path) {
        //TODO: folder must be empty
        File myObj = new File(path);
        if (myObj.delete()) {
            Log.debug("Deleted the file: " + myObj.getName());
        } else {
            Log.debug("Failed to delete the file.");
        }
    }

    private void writeConf() {
        List<String> conf = List.of("LOGGING_LEVEL=DEBUG",
                "TEMP_FILE_ENABLED=false",
                "TEMP_FILE=debugFile.txt",
                "PATH_TO_RESULTS=" + resultFolder,
                "RESULT_NAME_PREFIX=test-result_{nr}",
                "RUN_WITH_LLM=false",
                "PATH_TO_CODE=src/test/resources/");

        Util.write(testConfPath, "# properties used under testing - this file will be overwriten during test", false);
        conf.stream().forEach(line -> Util.write(testConfPath, line));
    }
}
