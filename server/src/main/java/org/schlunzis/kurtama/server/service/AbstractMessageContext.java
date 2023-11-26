package org.schlunzis.kurtama.server.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.ResponseAssembler;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

@Slf4j
@EqualsAndHashCode
@RequiredArgsConstructor
public class AbstractMessageContext {

    protected final ResponseAssembler responseAssembler;

    protected final AuthenticationService authenticationService;
    protected final ApplicationEventPublisher eventBus;

    @Getter
    protected final ISession session;
    @Getter
    protected final ServerUser user;

    public void respond(IServerMessage message) {
        responseAssembler.setMainResponse(new ServerMessageWrapper(message, session));
    }

    public void respondAdditionally(IServerMessage message) {
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, session));
    }

    /**
     * This method sends the given message to the given recipient.
     *
     * @param message   the message to send
     * @param recipient the user, which should receive the message
     */
    public void sendTo(IServerMessage message, ServerUser recipient) {
        authenticationService.getSessionForUser(recipient).ifPresentOrElse(
                s -> responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, s)),
                () -> log.warn("Could not get session for user")
        );
    }

    /**
     * This method sends the given message to the given recipients.
     *
     * @param message    the message to send
     * @param recipients the users, which should receive the message
     */
    public void sendToMany(IServerMessage message, Collection<ServerUser> recipients) {
        Collection<ISession> sessions = authenticationService.getSessionsForUsers(recipients);
        if (sessions.size() != recipients.size()) {
            log.warn("Tried to retrieve session for not logged-in users");
        }
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, sessions));
    }

    /**
     * This method sends the given message to all logged-in clients.
     *
     * @param message the message to send
     */
    public void sendToAll(IServerMessage message) {
        responseAssembler.addAdditionalMessage(new ServerMessageWrapper(message, authenticationService.getAllLoggedInSessions()));
    }

}
