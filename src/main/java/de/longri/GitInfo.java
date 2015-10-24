package de.longri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for get some Git infos
 */
public class GitInfo {

    private final File repository;

    private final String[] logCommand = new String[]{"git", "log", "-s", "-p", ""};

    /**
     * Constructor
     *
     * @param directory the path to repository
     */
    public GitInfo(String directory) {
        this.repository = new File((new File(directory)).getAbsolutePath());
    }

    /**
     * Constructor
     *
     * @param directory the path to repository
     */
    public GitInfo(File directory) {
        this.repository = directory;
    }

    /**
     * Returns the Committer of the given Path.
     * <p/>
     * If the given Path a File, so returned only the committer of this File.
     * Otherwise the committer of the given Directory.
     *
     * @return String
     */
    public String getCommitterWithCommitCount(String path) throws IOException, InterruptedException {

        if (path.startsWith("./")) {
            path = path.replace("./", repository.getAbsolutePath().replace(".", ""));
        }

        //check if path exists
        if (!new File(path).exists()) {
            throw new FileNotFoundException(path);
        }


        //get log
        logCommand[4] = "'" + path + "'";
        String log = RunCommand.runCommand(repository, logCommand);


        return filterCommitterWithCount(log);
    }

    /**
     * Returns the Committer of the given Path.
     * <p/>
     * If the given Path a File, so returned only the committer of this File.
     * Otherwise the committer of the given Directory.
     *
     * @return String
     */
    public String getCommitter(String path) {
        return "";
    }

    public String getAbsolutePath() {
        return repository.getAbsolutePath();
    }


    private String filterCommitterWithCount(String string) {
        StringBuilder sb = new StringBuilder();

        ArrayList<Committer> committerList = new ArrayList<Committer>();

        //split to Lines
        String[] lines = string.split("\n");

        for (int i = 0; i < lines.length - 1; i++) {

            //find commit line
            if (lines[i].startsWith("commit")) {
                String name = lines[++i];
                Committer committer = new Committer(name);
                if (committerList.contains(committer)) {
                    committerList.get(committerList.indexOf(committer)).committCount++;
                } else {
                    committerList.add(committer);
                }
            }
        }

        for (Committer committer : committerList) {
            sb.append(committer.committCount);
            sb.append(" ");
            sb.append(committer.name);
            sb.append("\n");
        }


        return sb.toString();
    }

    public class Committer {

        String name;
        int committCount = 0;

        Committer(String name) {
            this.name = name;
            committCount++;
        }

        @Override
        public boolean equals(Object other) {

            if (other == null) return false;

            if (other instanceof Committer) {
                Committer committer = (Committer) other;

                return (committer.name.equals(this.name));
            }

            return false;
        }
    }

}
