package de.longri;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Longri on 24.10.15.
 */
public class RunCommandTest extends TestCase {

    public void testRunCommand() throws Exception {
        String[] command = new String[]{"git", "log", "-s"};

        String result = RunCommand.runCommand(new File("./"), command);
        assertTrue(result.contains("commit ab671c4ea5c3d3d5fcf6b19c4a72c7652cc17080\n"));
    }

    //   /@Work/Longri/Gits/javadoc-helper/src/main/java/de/longri/GitInfo.java


    public void testRunCommand2() throws Exception {
        String[] command = new String[]{"git", "log", "-s", "-p", "/@Work/Longri/Gits/javadoc-helper/src/main/java/de/longri/GitInfo.java"};

        String result = RunCommand.runCommand(new File("./"), command);
        assertTrue(result.contains("commit ab671c4ea5c3d3d5fcf6b19c4a72c7652cc17080\n"));
    }

    public void testCommandListDir() {

        File execDir = new File("\\@Work\\Longri\\Gits\\cachebox");
        String returnValue = "";

        try {
            returnValue = RunCommand.runCommand(execDir, "git", "log", "-s", "-p", "\\@Work\\Longri\\Gits\\cachebox\\CB_Core\\src\\CB_Core\\Solver\\Functions\\FunctionInt.java");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(returnValue.contains("+ try and error for git shortlog"));
    }


    public void testRunCommandWithTimeOut() throws Exception {
        // the GIT-Command <git shortlog -s> gives always a time out!!!

        String[] command = new String[]{"git", "shortlog", "-s"};

        // test with default time out
        String result = RunCommand.runCommand(new File("./"), command);
        assertTrue(result.contains("RunCommand time out! after 10 SECONDS\n"));

        // test with 2 sec time out
        result = RunCommand.runCommand(new File("./"), 2, TimeUnit.SECONDS, command);
        assertTrue(result.contains("RunCommand time out! after 2 SECONDS\n"));
    }
}