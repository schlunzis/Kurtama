package org.schlunzis.kurtama.client.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.events.ClientClosingEvent;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.schlunzis.kurtama.client.events.NewServerConnectionEvent;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final NetworkServerFactory networkServerFactory;
    private INetworkClient networkClient;

    @EventListener
    public void onClientReadyEvent(ClientReadyEvent ignored) {
        networkClient = networkServerFactory.createNettyClient();
        startServer();
    }

    @EventListener
    public void onClientClosingEvent(ClientClosingEvent ignored) {
        networkClient.close();
    }

    @EventListener
    public void onNewServerConnectionEvent(NewServerConnectionEvent event) {
        networkClient.close();
        networkClient = networkServerFactory.createNettyClient(event.host(), event.port());
        startServer();
    }

    @EventListener
    void onClientMessage(IClientMessage clientMessage) {
        networkClient.sendMessage(clientMessage);
    }

    private void startServer() {
        Thread.ofVirtual()
                .name("Netty-Start-Thread")
                .start(() -> networkClient.start());
    }

}
