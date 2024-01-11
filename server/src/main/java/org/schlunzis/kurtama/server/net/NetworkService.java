package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.service.ServerMessageWrapper;
import org.schlunzis.kurtama.server.service.ServerMessageWrappers;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final INetworkServer nettyNetworkServer;

    @EventListener
    public void onServerMessage(IServerMessage serverMessage) {
        nettyNetworkServer.sendMessage(serverMessage);
    }

    @EventListener
    public void onMessageWrapper(ServerMessageWrapper messageWrapper) {
        log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
        Collection<ISession> nettySessions = messageWrapper.getRecipients().stream()
                .filter(s -> s.getSessionType().equals(SessionType.NETTY))
                .toList();
        nettyNetworkServer.sendMessage(messageWrapper.getServerMessage(), nettySessions);
    }

    @EventListener
    public void onMessageWrappers(ServerMessageWrappers messageWrappers) {
        // TODO optimize using org.schlunzis.kurtama.common.messages.ServerMessageBundle
        for (ServerMessageWrapper messageWrapper : messageWrappers.wrappers()) {
            log.debug("Sending message {} to {} recipients", messageWrapper.getServerMessage(), messageWrapper.getRecipients().size());
            onMessageWrapper(messageWrapper);
        }
    }

}
