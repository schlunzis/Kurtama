package de.schlunzis.client.net;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NetworkStartThread extends Thread {

    private final NetworkClient networkClient;

    @Override
    public void run() {
        networkClient.start();
    }

}
