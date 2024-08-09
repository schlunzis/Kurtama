package org.schlunzis.kurtama.client.server;

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

    public abstract boolean testRequirements();

    public abstract void run(int port, String path);

    public void stop() {
        if (serverProcess != null)
            serverProcess.destroy();
    }

}
