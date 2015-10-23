package de.longri;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;


/**
 * JUnit test for check Git command blame
 * Created by Longri on 22.10.2015.
 */
public class commandBlame {

    @Test
    public void testCommandListDir() {

        File execDir = new File("C:\\@Work\\Longri\\Gits\\cachebox - Kopie");
        String returnValue = "";

        try {
            returnValue = RunCommand.runCommand(execDir, "git", "log", "-s", "-p", "C:\\@Work\\Longri\\Gits\\cachebox - Kopie\\CB_Core\\src\\CB_Core\\Solver\\Functions\\FunctionInt.java");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(returnValue.contains("+ try and error for git shortlog"));
    }


    @Test
    public void testCommandGitShortlog() {

        File execDir = new File("./");
        String returnValue = "";

        try {
            returnValue = RunCommand.runCommand(execDir, 10, TimeUnit.SECONDS, "git", "shortlog", "--summary");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(returnValue.equals(""));
    }
}
