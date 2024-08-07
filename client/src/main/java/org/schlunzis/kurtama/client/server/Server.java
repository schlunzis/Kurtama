package org.schlunzis.kurtama.client.server;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class Server {

    private static final int MINIMUM_JAVA_VERSION = 22;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });
    private Process serverProcess;

    @Setter
    private ServerType serverType;

    public boolean testRequirements() {
        return switch (serverType) {
            case JAR -> testJAR();
            case DOCKER -> testDocker();
        };
    }

    private boolean testJAR() {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-version");
        try {
            Process process = processBuilder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getErrorStream());
            executor.submit(streamGobbler);

            int exitCode = process.waitFor();
            String[] output = streamGobbler.getOutput();
            Arrays.stream(output).forEach(log::info);
            String javaVersion = extractJavaVersion(output);
            log.info("Java version: {}", javaVersion);

            return exitCode == 0 && javaVersion != null && Integer.parseInt(javaVersion) >= MINIMUM_JAVA_VERSION;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error while testing JAR requirements", e);
            return false;
        } catch (IOException e) {
            log.error("Error while testing JAR requirements", e);
        }
        return false;
    }

    public static String extractJavaVersion(String[] output) {
        String lineWithVersion = output[1];
        int startIndex = lineWithVersion.indexOf("(");
        int endIndex = lineWithVersion.indexOf(")");
        String semverString = lineWithVersion.substring(startIndex + 1, endIndex).split(" ")[1];
        log.debug("Semver string: {}", semverString);

        return semverString.split("\\.")[0];
    }

    private boolean testDocker() {
        return false;
    }

    public void run(int port) {
    }

}
