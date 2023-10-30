package de.schlunzis.client.net;

import de.schlunzis.common.messages.IClientMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final INetworkClient networkClient;


    @EventListener
    void onClientMessage(IClientMessage clientMessage) {
        networkClient.sendMessage(clientMessage);
    }

}
