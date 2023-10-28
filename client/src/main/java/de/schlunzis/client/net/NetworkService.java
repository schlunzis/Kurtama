package de.schlunzis.client.net;

import de.schlunzis.common.messages.ClientMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final NetworkClient networkClient;


    @EventListener
    void onClientMessage(ClientMessage clientMessage) {
        networkClient.sendMessage(clientMessage);
    }

}
