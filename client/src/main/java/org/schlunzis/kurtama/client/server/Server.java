package org.schlunzis.kurtama.client.server;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private static final String JAR_URL = "https://github.com/schlunzis/Kurtama/releases/download/v${kurtama-version}/kurtama-client-${kurtama-version}-linux.jar";
    private static final String JAR_URL_VERSION_REPLACEMENT = "${kurtama-version}";
    private static final String JAR_VERSION = "0.0.1-alpha+20240807142035";
    private static final String JAR_PATH = "kurtama-server.jar";

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
        log.info("Testing for java installation");
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
        log.info("Testing for docker installation");
        ProcessBuilder processBuilderDocker = new ProcessBuilder("docker", "--version");
        ProcessBuilder processBuilderCompose = new ProcessBuilder("docker", "compose", "version");
        try {
            Process processDocker = processBuilderDocker.start();
            StreamGobbler streamGobbler = new StreamGobbler(processDocker.getInputStream());
            executor.submit(streamGobbler);
            int exitCodeDocker = processDocker.waitFor();
            String[] output = streamGobbler.getOutput();
            Arrays.stream(output).forEach(log::info);

            Process processCompose = processBuilderCompose.start();
            streamGobbler = new StreamGobbler(processCompose.getInputStream());
            executor.submit(streamGobbler);
            int exitCodeCompose = processCompose.waitFor();
            output = streamGobbler.getOutput();
            Arrays.stream(output).forEach(log::info);

            return exitCodeDocker == 0 && exitCodeCompose == 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error while testing DOCKER requirements", e);
            return false;
        } catch (IOException e) {
            log.error("Error while testing DOCKER requirements", e);
        }
        return false;
    }

    public void run(int port) {
        switch (serverType) {
            case JAR -> runJAR(port);
            case DOCKER -> runDocker(port);
        }
    }

    private void runJAR(int port) {
        log.info("Starting server with JAR");
        executor.submit(() -> {
            try {
                FileUtils.copyURLToFile(
                        new URI(JAR_URL.replace(JAR_URL_VERSION_REPLACEMENT, JAR_VERSION)).toURL(),
                        new File(JAR_PATH),
                        10000,
                        10000
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", JAR_PATH)
                    .directory(new File("."))
                    .inheritIO();
            processBuilder.environment().put("KURTAMA_SERVER_PORT", String.valueOf(port));
            try {
                serverProcess = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void runDocker(int port) {

    }

}
