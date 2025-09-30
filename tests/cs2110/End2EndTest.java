package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class End2EndTest {

    /**
     * The original `System.out`.
     */
    static PrintStream systemOut;

    /**
     * Replacement for `System.out` during test execution.
     */
    static PrintStream out;
    static ByteArrayOutputStream outBytes;

    @BeforeAll
    static void captureOutput() {
        outBytes = new ByteArrayOutputStream();
        out = new PrintStream(outBytes);
        systemOut = System.out;
        System.setOut(out);
    }

    @AfterAll
    static void restoreOutput() {
        out.close();
        System.setOut(systemOut);
    }

    /**
     * Checks that each line produced by these two scanners are the same, which avoids platform
     * specific line ending anomalies.
     */
    void assertSameLines(Scanner expectedSc, Scanner actualSc) {
        int line = 1;
        while (expectedSc.hasNextLine()) {
            String expectedLine = expectedSc.nextLine();
            assertTrue(actualSc.hasNextLine(), "Simulation output has fewer lines than expected.");
            String actualLine = actualSc.nextLine();
            assertEquals(expectedLine, actualLine, "Error on line " + line++);
        }
        assertFalse(actualSc.hasNextLine(), "Simulation output has more lines than expected.");
    }

    @DisplayName(
            "WHEN the commands in 'sample_game_inputs.txt' are executed, the simulator produces "
                    + "the correct output as specified in `sample_game_expected_output.txt")
    @Test
    void runEnd2End() throws FileNotFoundException {
        GameEngine.main(new String[]{"sample_game_inputs.txt"});

        out.flush();
        Scanner actualSc = new Scanner(outBytes.toString());
        
        File expectedFile = new File("sample_game_expected_output.txt");
        Scanner expectedSc = new Scanner(expectedFile);

        assertSameLines(expectedSc, actualSc);
    }

}
