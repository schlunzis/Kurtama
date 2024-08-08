package org.schlunzis.kurtama.client.server;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class Server {

    protected static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    protected Process serverProcess;

    public abstract boolean testRequirements();

    public abstract void run(int port, String path);

    public abstract void stop();

}
