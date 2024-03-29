package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.net.netty.NettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class NetworkServerInitializer {

    private final NetworkService networkService;

    private final NettyServer nettyServer;
    @Value("${kurtama.server.netty.enable}")
    private boolean nettyEnable;

    public void init() {
        if (nettyEnable) {
            Thread.ofVirtual()
                    .name("Netty-Start-Thread")
                    .start(() -> {
                        networkService.addServer(SessionType.NETTY, nettyServer);
                        nettyServer.start();
                    });
        }
        new Semaphore(0).acquireUninterruptibly();
    }

}
