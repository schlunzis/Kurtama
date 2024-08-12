package org.schlunzis.kurtama.client.net;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.net.impl.NettyClient;
import org.schlunzis.kurtama.client.settings.IUserSettings;
import org.schlunzis.kurtama.client.settings.Setting;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NetworkServerFactory {

    private final ApplicationEventPublisher eventBus;
    private final IUserSettings userSettings;
    private final ServerMessageDispatcher serverMessageDispatcher;


    public INetworkClient createNettyClient() {
        return createNettyClient(userSettings.getString(Setting.HOST), userSettings.getInt(Setting.PORT));
    }

    public INetworkClient createNettyClient(String host, int port) {
        return new NettyClient(serverMessageDispatcher, eventBus, host, port);
    }

}
