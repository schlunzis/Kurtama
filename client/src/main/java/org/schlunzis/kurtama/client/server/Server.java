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

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
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

            return exitCode == 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error while testing JAR requirements", e);
            return false;
        } catch (IOException e) {
            log.error("Error while testing JAR requirements", e);
        }
        return false;
    }

    private boolean testDocker() {
        return false;
    }

    public void run(int port) {
    }

}
