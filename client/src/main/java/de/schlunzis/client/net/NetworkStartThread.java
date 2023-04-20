package de.schlunzis.client.net;

import org.springframework.stereotype.Component;

@Component
public class NetworkStartThread extends Thread {

    private final NetworkClient networkClient;

    public NetworkStartThread(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @Override
    public void run() {
        networkClient.start();
    }

}
