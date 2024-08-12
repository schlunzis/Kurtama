package org.schlunzis.kurtama.client.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.schlunzis.kurtama.client.util.VersionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
class DockerServer extends Server {

    private final String COMPOSE_STRING;
    private String path;

    DockerServer(VersionManager versionManager, LogSink logSink) {
        super(logSink);
        try {
            String cs = IOUtils.resourceToString("/server/server-compose.yml", StandardCharsets.UTF_8);
            this.COMPOSE_STRING = cs.replace("{version}", versionManager.getDockerVersion());
        } catch (IOException e) {
            log.error("Error while reading docker-compose.yml", e);
            throw new RuntimeException("Error while reading docker-compose.yml", e);
        }
    }

    @Override
    public boolean testRequirements() {
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

    @Override
    public void run(int port, String path) {
        setStatus(ServerStatus.RUNNING);
        String runConfig = COMPOSE_STRING.replace("{port}", String.valueOf(port));
        // write runConfig to docker-compose.yml file at path
        try {
            FileUtils.writeStringToFile(new File(path + File.separator + "docker-compose.yml"), runConfig, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            log.error("Error while writing docker-compose.yml", e);
            setStatus(ServerStatus.RUNNING_FAILED);
            return;
        }

        executor.submit(() -> {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "compose", "up")
                    .directory(new File(path));
            this.path = path;
            try {
                serverProcess = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream()));
                while (serverProcess.isAlive()) {
                    logSink.log(reader.readLine());
                }
            } catch (IOException e) {
                log.error("Error while starting server", e);
                setStatus(ServerStatus.RUNNING_FAILED);
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        if (path == null) {
            return;
        }

        ProcessBuilder processBuilder = new ProcessBuilder("docker", "compose", "down")
                .directory(new File(path));
        try {
            processBuilder.start();
            setStatus(ServerStatus.STOPPED);
        } catch (IOException e) {
            log.error("Error while stopping server", e);
            setStatus(ServerStatus.RUNNING_FAILED);
        }
    }
}
