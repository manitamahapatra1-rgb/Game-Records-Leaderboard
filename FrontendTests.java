// CS 400 Sam Pallan P103.RoleCode
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

/**
 * frontend tests for P103.RoleCode
 * [IMPORTANT] currently relies on Backend_Placeholder and Tree_Placeholder
 */
public class FrontendTests {

    /**
     * roleTest1: verifies that help text is shown when the user types "help",
     * and that the command loop can quit
     */
    @Test
    public void roleTest1() {
        TextUITester tester = new TextUITester(
                "help\n" +
                "quit\n"
        );

        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());
        Frontend frontend = new Frontend(new Scanner(System.in), backend);
        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Commands"), "Should print command instructions header.");
        assertTrue(output.contains("submit"), "Help should mention submit command.");
        assertTrue(output.contains("quit"), "Help should mention quit command.");
    }

    /**
     * roleTest2: runs submit + show to ensure the frontend calls backend.addRecord()
     * and prints names returned by backend.getTopTen().
     *
     * Backend_Placeholder ignores the submitted record and inserts "ne0nVandal".
     */
    @Test
    public void roleTest2() {
        TextUITester tester = new TextUITester(
                "submit Alice NORTH_AMERICA 100 5 10 001:02:03\n" +
                "show 10\n" +
                "quit\n"
        );

        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());
        Frontend frontend = new Frontend(new Scanner(System.in), backend);
        frontend.runCommandLoop();

        String output = tester.checkOutput();
        assertTrue(output.contains("Submitted record"), "Should confirm submit succeeded.");
        assertTrue(output.contains("ne0nVandal"),
                "Placeholder backend should add ne0nVandal; show should print it.");
    }

    /**
     * roleTest3: runs the remaining command categories at least once:
     * - submit multiple (readData)
     * - level (getAndSetRange)
     * - time (applyAndSetFilter)
     * - show most collectables
     *
     * Backend_Placeholder inserts "voidR1fter" when readData is called
     */
    @Test
    public void roleTest3() {
        TextUITester tester = new TextUITester(
                "submit multiple records.csv\n" +
                "level 1 to 999\n" +
                "time 010:00:00\n" +
                "show most collectables\n" +
                "quit\n"
        );

        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());
        Frontend frontend = new Frontend(new Scanner(System.in), backend);
        frontend.runCommandLoop();

        // dear god i love JUnit tests why didn't we get this is CS300???
        String output = tester.checkOutput();
        assertTrue(output.contains("Loaded records"), "Should confirm file load.");
        assertTrue(output.contains("Level range set"), "Should confirm level range set.");
        assertTrue(output.contains("Time filter set"), "Should confirm time filter set.");
        assertTrue(output.contains("voidR1fter"),
                "Placeholder backend adds voidR1fter on readData; show should print it.");
    }
}