package de.schlunzis.client.net;

import de.schlunzis.client.events.ClientReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NetworkStartThread extends Thread {

    private final NetworkClient networkClient;

    @EventListener
    public void onClientReadyEvent(ClientReadyEvent event) {
        this.start();
    }

    @Override
    public void run() {
        networkClient.start();
    }

}
