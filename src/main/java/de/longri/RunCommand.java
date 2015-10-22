package de.longri;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class RunCommand {

    private static final long DEFAULT_TIME_OUT = 10;

    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

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


        if (!p.waitFor(6, TimeUnit.SECONDS)) {
            //timeout - kill the process.
            p.destroy(); // consider using destroyForcibly instead
            timeOut = true;
        }

        if (timeOut) {
            //write time out msg
            sb.append("RunCommand time out! after " + timeout + " " + unit.toString());
            sb.append("     directory: " + directory.getAbsolutePath());
            sb.append("     command: " + command);
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

