package org.schlunzis.kurtama.client.net;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.net.impl.ClientHandler;
import org.schlunzis.kurtama.client.net.impl.NettyClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NetworkServerFactory {

    private final ApplicationEventPublisher eventBus;
    private final NetworkSettings networkSettings;
    private final ServerMessageDispatcher serverMessageDispatcher;


    public INetworkClient createNettyClient() {
        return new NettyClient(new ClientHandler(serverMessageDispatcher), eventBus, networkSettings.getHost(), networkSettings.getPort());
    }

    public INetworkClient createNettyClient(String host, int port) {
        return new NettyClient(new ClientHandler(serverMessageDispatcher), eventBus, host, port);
    }

}
