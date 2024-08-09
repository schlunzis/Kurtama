package org.schlunzis.kurtama.client.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class DockerServer extends Server {

    public DockerServer(LogSink logSink) {
        super(logSink);
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

    }

}
