package de.schlunzis.client.net;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.common.messages.ClientMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NetworkService {

    private final NetworkClient networkClient;

    NetworkService(EventBus eventBus, NetworkClient networkClient) {
        this.networkClient = networkClient;
        eventBus.register(this);
    }

    @Subscribe
    void onClientMessage(ClientMessage clientMessage) {
        networkClient.sendMessage(clientMessage);
    }

}
