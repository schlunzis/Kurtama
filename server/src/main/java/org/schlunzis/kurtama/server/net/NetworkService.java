package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.service.ServerMessageWrapper;
import org.schlunzis.kurtama.server.service.ServerMessageWrappers;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final Map<SessionType, INetworkServer> servers = new EnumMap<>(SessionType.class);

    /**
     * Registers a INetworkServer to send messages to. The SessionType is used to differentiate message, which should be
     * sent to all kinds of servers.
     *
     * @param sessionType the type of the session
     * @param server      the server to register
     * @return the previous server registered for this session. empty if no server was registered before.
     */
    public Optional<INetworkServer> addServer(SessionType sessionType, INetworkServer server) {
        Objects.requireNonNull(sessionType);
        Objects.requireNonNull(server);
        return Optional.ofNullable(servers.put(sessionType, server));
    }

    /**
     * Sends the given message to all connected clients via all registered server implementations. If the given message
     * is null, the message is ignored and the implementations are not informed about the message.
     *
     * @param serverMessage the message to send
     */
    @EventListener
    public void onServerMessage(IServerMessage serverMessage) {
        if (serverMessage != null)
            servers.values().forEach(server -> server.sendMessage(serverMessage));
        else
            log.debug("message is null. ignoring");
    }

    /**
     * Sends the given message to all given sessions via the relevant registered server implementations. If the given
     * message or the recipients list is null or empty, the message is ignored and the implementations are not informed
     * about the message. If sessions are null or one of its components are null, the session is ignored and count
     * towards emptying the list.
     *
     * @param messageWrapper the message to send
     */
    @EventListener
    public void onMessageWrapper(ServerMessageWrapper messageWrapper) {
        Collection<ISession> filteredSessions = verifyServerMessageWrapper(messageWrapper);
        if (filteredSessions.isEmpty()) {
            log.debug("Malformed ServerMessageWrapper! ignoring message");
            return;
        }

        log.debug("Sending message to {} recipients", filteredSessions.size());
        for (var entry : servers.entrySet()) {
            Collection<ISession> sessions = filteredSessions.stream()
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

    private Collection<ISession> verifyServerMessageWrapper(ServerMessageWrapper messageWrapper) {
        if (messageWrapper == null || messageWrapper.getServerMessage() == null ||
                messageWrapper.getRecipients() == null || messageWrapper.getRecipients().isEmpty()) {
            return Collections.emptyList();
        }
        return messageWrapper.getRecipients().stream()
                .<ISession>mapMulti(((session, downstream) -> {
                    if (session == null || session.sessionType() == null || session.id() == null)
                        log.debug("Malformed ServerMessageWrapper! ignoring session");
                    else downstream.accept(session);
                }))
                .toList();
    }

}
