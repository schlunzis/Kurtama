package org.schlunzis.kurtama.client.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IClientMessage;
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
