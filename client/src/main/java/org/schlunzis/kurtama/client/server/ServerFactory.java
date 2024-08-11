package org.schlunzis.kurtama.client.server;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.util.VersionManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerFactory {

    private final VersionManager versionManager;
    private final LogSink logSink;

    public Server getServer(ServerType serverType) {
        return switch (serverType) {
            case JAR -> new JarServer(versionManager, logSink);
            case DOCKER -> new DockerServer(versionManager, logSink);
        };
    }

}
