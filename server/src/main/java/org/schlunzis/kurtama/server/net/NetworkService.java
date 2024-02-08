package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.net.netty.NettyServer;
import org.schlunzis.kurtama.server.service.ServerMessageWrapper;
import org.schlunzis.kurtama.server.service.ServerMessageWrappers;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final Map<SessionType, INetworkServer> servers = new EnumMap<>(SessionType.class);

    public void addServer(SessionType sessionType, NettyServer nettyServer) {
        servers.put(sessionType, nettyServer);
    }

    /**
     * send to all
     *
     * @param serverMessage the message to send
     */
    @EventListener
    public void onServerMessage(IServerMessage serverMessage) {
        servers.values().forEach(server -> server.sendMessage(serverMessage));
    }

    @EventListener
    public void onMessageWrapper(ServerMessageWrapper messageWrapper) {
        log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
        for (var entry : servers.entrySet()) {
            Collection<ISession> sessions = messageWrapper.getRecipients().stream()
                    .filter(s -> s.sessionType().equals(entry.getKey()))
                    .toList();
            entry.getValue().sendMessage(messageWrapper.getServerMessage(), sessions);
        }
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
