package org.schlunzis.kurtama.client.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.schlunzis.kurtama.client.util.VersionManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JarServer extends Server {

    private static final int MINIMUM_JAVA_VERSION = 22;
    private static final String JAR_URL = "https://github.com/schlunzis/Kurtama/releases/download/v${kurtama-version}/kurtama-server-${kurtama-version}.jar";
    private static final String JAR_URL_VERSION_REPLACEMENT = "${kurtama-version}";
    private static final String JAR_PATH = "kurtama-server.jar";

    private final VersionManager versionManager;

    @Override
    public boolean testRequirements() {
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

    private static String extractJavaVersion(String[] output) {
        String lineWithVersion = output[1];
        int startIndex = lineWithVersion.indexOf("(");
        int endIndex = lineWithVersion.indexOf(")");
        String semverString = lineWithVersion.substring(startIndex + 1, endIndex).split(" ")[1];
        log.debug("Semver string: {}", semverString);

        return semverString.split("\\.")[0];
    }

    @Override
    public void run(int port, String path) {
        log.info("Starting server with JAR");
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
                throw new RuntimeException(e);
            }

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", JAR_PATH)
                    .directory(new File(path))
                    .inheritIO();
            processBuilder.environment().put("KURTAMA_SERVER_PORT", String.valueOf(port));
            try {
                serverProcess = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
