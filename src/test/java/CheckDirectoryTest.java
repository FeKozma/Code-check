import CodeCheck.CheckDirectory;
import CodeCheck.ManyFunctions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckDirectoryTest {
    @Test
    public void checkDirectory() {
        File file = new File(getClass().getResource("checkDirectoryTest1").getFile());
        ManyFunctions functions = new CheckDirectory().checkDirectory(file);

        assertEquals(4, functions.oneFunctions.size());
    }
}
