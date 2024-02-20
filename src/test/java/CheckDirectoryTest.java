import CodeCheck.CheckDirectory;
import CodeCheck.ManyFunctions;
import CodeCheck.Util;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckDirectoryTest {
    @Test
    public void checkDirectory() {
        File file = new File(getClass().getResource("checkDirectoryTest1").getFile());
        ManyFunctions functions = new CheckDirectory().checkDirectory(file);

        assertEquals(4, functions.oneFunctions.size());
    }

    @Test
    public void checkIfCorrectPath() {
        String file = new File(getClass().getResource("checkDirectoryTest1").getFile()).getPath();
        assertEquals(file, Util.checkIfHomePath(file));
    }

    @Test
    public void checkIfCorrectAbsolutePath() {
        String file = new File(getClass().getResource("checkDirectoryTest1").getFile()).getAbsolutePath();
        assertEquals(file, Util.checkIfHomePath(file));
    }

    @Test
    public void checkIfHomePath() {
        assertEquals(System.getProperty("user.home"), Util.checkIfHomePath("~"));
    }

    @Test
    public void checkIfHomePathWithFolder() {
        assertEquals(System.getProperty("user.home") + "/projects", Util.checkIfHomePath("~/projects"));
    }
}
