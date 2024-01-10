package org.schlunzis.kurtama.server.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class AbstractMessageContext {

    protected final ResponseAssembler responseAssembler;

    protected final ApplicationEventPublisher eventBus;

    @Getter
    protected final ISession session;
    @Getter
    protected final ServerUser user;

    public void respondAdditionally(IServerMessage message) {
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, user));
    }

    /**
     * This method sends the given message to the given recipient.
     *
     * @param message   the message to send
     * @param recipient the user, which should receive the message
     */
    public void sendTo(IServerMessage message, ServerUser recipient) {
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, recipient));
    }

    /**
     * This method sends the given message to the given recipients.
     *
     * @param message    the message to send
     * @param recipients the users, which should receive the message
     */
    public void sendToMany(IServerMessage message, Collection<ServerUser> recipients) {
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, recipients));
    }

    /**
     * This method sends the given message to all logged-in clients.
     *
     * @param message the message to send
     */
    public void sendToAll(IServerMessage message) {
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, Collections.emptyList()));
    }

}
