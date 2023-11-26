package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final INetworkServer networkServer;


    public void start() {
        networkServer.start();
    }

    @EventListener
    public void onServerMessage(IServerMessage serverMessage) {
        networkServer.sendMessage(serverMessage);
    }

    @EventListener
    public void onMessageWrapper(ServerMessageWrapper messageWrapper) {
        log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
        networkServer.sendMessage(messageWrapper.getServerMessage(), messageWrapper.getRecipients());
    }

    @EventListener
    public void onMessageWrappers(ServerMessageWrappers messageWrappers) {
        // TODO optimize using org.schlunzis.kurtama.common.messages.ServerMessageBundle
        for (ServerMessageWrapper messageWrapper : messageWrappers.wrappers()) {
            log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
            networkServer.sendMessage(messageWrapper.getServerMessage(), messageWrapper.getRecipients());
        }
    }

}
