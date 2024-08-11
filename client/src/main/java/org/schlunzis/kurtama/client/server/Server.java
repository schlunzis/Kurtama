package org.schlunzis.kurtama.client.server;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public abstract class Server {

    protected static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    protected final LogSink logSink;

    protected Process serverProcess;

    private final ObjectProperty<ServerStatus> status = new SimpleObjectProperty<>(ServerStatus.NOT_STARTED);

    public abstract boolean testRequirements();

    public abstract void run(int port, String path);

    public void stop() {
        if (serverProcess != null)
            serverProcess.destroy();
        setStatus(ServerStatus.STOPPED);
    }

    protected void setStatus(ServerStatus status) {
        log.info("Server status: {}", status);
        this.status.set(status);
    }

    public ObjectProperty<ServerStatus> statusProperty() {
        return status;
    }

    public ServerStatus getStatus() {
        return status.get();
    }

}
