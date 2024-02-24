package org.schlunzis.kurtama.client.net;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.net.impl.ClientHandler;
import org.schlunzis.kurtama.client.net.impl.NettyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NetworkServerFactory {

    private final ApplicationEventPublisher eventBus;
    private final ClientHandler nettyClientHandler;

    @Value("${kurtama.server.port}")
    private int port;
    @Value("${kurtama.server.host}")
    private String host;


    public INetworkClient createNettyClient() {
        return new NettyClient(nettyClientHandler, eventBus, host, port);
    }

    public INetworkClient createNettyClient(String host, int port) {
        return new NettyClient(nettyClientHandler, eventBus, host, port);
    }

}
