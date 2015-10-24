package de.longri;

import junit.framework.TestCase;

import java.io.File;

/**
 * Created by Longri on 24.10.15.
 */
public class GitInfoTest extends TestCase {

    public void testConstructor() {
        // the both constructors must give the same path

        GitInfo git1 = new GitInfo("./");
        GitInfo git2 = new GitInfo(new File("./"));

        assertEquals(git1.getAbsolutePath(), git2.getAbsolutePath());

    }


    public void testGetCommitterWithCommitCount() throws Exception {
        GitInfo git = new GitInfo("./");

        String result = git.getCommitterWithCommitCount("./src/main/java/de/longri/GitInfo.java");
        assertEquals(result, "");
    }

    public void testGetCommitter() throws Exception {
//TODO write test
    }
}