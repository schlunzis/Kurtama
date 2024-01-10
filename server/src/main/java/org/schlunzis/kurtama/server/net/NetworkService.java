package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.netty.NettySession;
import org.schlunzis.kurtama.server.service.ServerMessageWrapper;
import org.schlunzis.kurtama.server.service.ServerMessageWrappers;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final AuthenticationService authenticationService;

    private final INetworkServer<NettySession> nettyNetworkServer;

    @EventListener
    public void onServerMessage(IServerMessage serverMessage) {
        nettyNetworkServer.sendMessage(serverMessage);
    }

    @EventListener
    public void onMessageWrapper(ServerMessageWrapper messageWrapper) {
        log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
        Collection<ISession> sessions = authenticationService.getSessionsForUsers(messageWrapper.getRecipients());
        Collection<NettySession> nettySessions = sessions.stream()
                .map(s -> s instanceof NettySession ns ? ns : null)
                .filter(Objects::nonNull)
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
