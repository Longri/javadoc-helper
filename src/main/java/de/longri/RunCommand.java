package de.longri;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Longri 2015
 */
public class RunCommand {

    private static final long DEFAULT_TIME_OUT = 10;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;


    private RunCommand() {
    } // disable constructor, only static member

    public static String runCommand(File directory, String... command) throws IOException, InterruptedException {
        return runCommand(directory, DEFAULT_TIME_OUT, DEFAULT_TIME_UNIT, command);
    }

    public static String runCommand(File directory, long timeout, TimeUnit unit, String... command) throws IOException, InterruptedException {

        StringBuilder sb = new StringBuilder();

        ProcessBuilder pb = new ProcessBuilder(command)
                .redirectErrorStream(true).directory(directory);


        Process p = pb.start();

        InputStream is = p.getInputStream();
        InputStream es = p.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        BufferedReader bre = new BufferedReader(new InputStreamReader(es));
        String in;

        boolean timeOut = false;


        if (!p.waitFor(timeout, unit)) {
            //timeout - kill the process.
            p.destroy(); // consider using destroyForcibly instead
            timeOut = true;
        }

        if (timeOut) {
            //write time out msg
            sb.append("RunCommand time out! after " + timeout + " " + unit.toString() + "\n");
            sb.append("     directory: " + directory.getAbsolutePath() + "\n");
            sb.append("     command: " + command + "\n");
        } else {
            while ((in = br.readLine()) != null) {
                sb.append(in + "\n");
            }

            while ((in = bre.readLine()) != null) {
                sb.append(in + "\n");
            }
        }
        return sb.toString();
    }
}

