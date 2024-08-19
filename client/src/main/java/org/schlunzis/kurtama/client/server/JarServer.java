package org.schlunzis.kurtama.client.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.schlunzis.kurtama.client.util.VersionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
class JarServer extends Server {

    private static final int MINIMUM_JAVA_VERSION = 22;
    private static final String JAR_URL = "https://github.com/schlunzis/Kurtama/releases/download/v${kurtama-version}/kurtama-server-${kurtama-version}.jar";
    private static final String JAR_URL_VERSION_REPLACEMENT = "${kurtama-version}";
    private static final String JAR_PATH = "kurtama-server.jar";

    private final VersionManager versionManager;

    private final Semaphore mutex = new Semaphore(1);
    private String javaExecutable = null;

    JarServer(VersionManager versionManager, LogSink logSink) {
        super(logSink);
        this.versionManager = versionManager;
    }

    @Override
    public boolean testRequirements() {
        if (!testUsingJavaHome()) {
            log.info("Testing using JAVA_HOME failed, trying PATH");
            return testUsingPath();
        }
        return true;
    }

    private boolean testUsingJavaHome() {
        log.info("Testing for java installation using JAVA_HOME");
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            log.info("JAVA_HOME not set");
            return false;
        }

        String testExecutable = javaHome + File.separator + "bin" + File.separator + "java";
        if (test(testExecutable)) {
            javaExecutable = testExecutable;
            return true;
        }
        return false;
    }

    private boolean testUsingPath() {
        String path = System.getenv("PATH");
        if (path == null) {
            log.info("PATH not set");
            return false;
        }

        String[] pathEntries = path.split(File.pathSeparator);
        for (String pathEntry : pathEntries) {
            String testExecutable = pathEntry + File.separator + "java";
            if (test(testExecutable)) {
                javaExecutable = testExecutable;
                return true;
            }
        }
        return false;
    }

    private boolean test(String javaExecutable) {
        log.info("Testing for java installation using executable: {}", javaExecutable);
        ProcessBuilder processBuilder = new ProcessBuilder(javaExecutable, "-version");
        try {
            Process process = processBuilder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getErrorStream());
            executor.submit(streamGobbler);

            int exitCode = process.waitFor();
            String[] output = streamGobbler.getOutput();
            Arrays.stream(output).forEach(log::info);
            int javaVersion = extractJavaVersion(output);
            log.info("Java version: {}", javaVersion);

            return exitCode == 0 && javaVersion >= MINIMUM_JAVA_VERSION;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error while testing JAR requirements", e);
            return false;
        } catch (IOException e) {
            log.debug("Error while testing JAR requirements", e);
        }
        return false;
    }

    private static int extractJavaVersion(String[] output) {
        String lineWithVersion = output[1];
        int startIndex = lineWithVersion.indexOf("(");
        int endIndex = lineWithVersion.indexOf(")");
        String semverString = lineWithVersion.substring(startIndex + 1, endIndex).split(" ")[1];
        log.debug("Semver string: {}", semverString);

        // Extract major version by returning the first number in the string
        Pattern pattern = Pattern.compile("^\\d+");
        Matcher matcher = pattern.matcher(semverString);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            log.error("Error while extracting Java version");
        }
        return -1;
    }

    @Override
    public void run(int port, String path) {
        log.info("Downloading server with JAR");
        mutex.acquireUninterruptibly();
        if (getStatus() != ServerStatus.NOT_STARTED) {
            log.info("Server already running");
            return;
        }
        setStatus(ServerStatus.DOWNLOADING);
        executor.submit(() -> {
            try {
                FileUtils.copyURLToFile(
                        new URI(JAR_URL.replace(JAR_URL_VERSION_REPLACEMENT, versionManager.getVersion())).toURL(),
                        new File(path + File.separator + JAR_PATH),
                        10000,
                        10000
                );
            } catch (IOException | URISyntaxException e) {
                log.error("Error while downloading JAR", e);
                setStatus(ServerStatus.DOWNLOAD_FAILED);
                return;
            }

            log.info("Starting server with JAR");
            setStatus(ServerStatus.STARTING);
            ProcessBuilder processBuilder = new ProcessBuilder(javaExecutable, "-jar", JAR_PATH)
                    .directory(new File(path));
            processBuilder.environment().put("KURTAMA_SERVER_PORT", String.valueOf(port));
            try {
                serverProcess = processBuilder.start();
                serverProcess.onExit().thenRun(() -> setStatus(ServerStatus.STOPPED));
                try (BufferedReader reader = serverProcess.inputReader()) {
                    while (serverProcess.isAlive()) {
                        String line = reader.readLine();
                        if (getStatus() == ServerStatus.STARTING && line.endsWith("Netty Server started on port: " + port)) {
                            setStatus(ServerStatus.RUNNING);
                            mutex.release();
                        }
                        logSink.log(line);
                    }
                }
            } catch (IOException e) {
                log.error("Error while starting JAR server", e);
                setStatus(ServerStatus.RUN_FAILED);
            }
        });
    }

    @Override
    public void stop() {
        setStatus(ServerStatus.STOPPING);
        mutex.acquireUninterruptibly();
        super.stop();
        mutex.release();
    }

}
