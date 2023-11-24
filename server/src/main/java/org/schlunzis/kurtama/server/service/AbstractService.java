package org.schlunzis.kurtama.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.auth.IAuthenticationService;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * A base class for all services, except the {@link org.schlunzis.kurtama.server.auth.AuthenticationService}.
 * <p>
 * This class provides basic functionality, for the services to send messages to users not sessions.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService {

    protected final ApplicationEventPublisher eventBus;
    protected final IAuthenticationService authenticationService;

    private final SecondaryRequestHandler secondaryRequestHandler;

    /**
     * This method sends the given message to all logged-in clients.
     *
     * @param message the message to send
     */
    protected void sendToAll(IServerMessage message) {
        eventBus.publishEvent(new ServerMessageWrapper(message, authenticationService.getAllLoggedInSessions()));
    }

    /**
     * This method sends the given message to the given recipients.
     *
     * @param message    the message to send
     * @param recipients the users, which should receive the message
     */
    protected void sendToMany(IServerMessage message, Collection<ServerUser> recipients) {
        Collection<ISession> sessions = authenticationService.getSessionsForUsers(recipients);
        if (sessions.size() != recipients.size()) {
            log.warn("Tried to retrieve session for not logged-in users");
        }
        eventBus.publishEvent(new ServerMessageWrapper(message, sessions));
    }

    /**
     * This method sends the given message to the given recipient.
     *
     * @param message   the message to send
     * @param recipient the user, which should receive the message
     */
    protected void sendTo(IServerMessage message, ServerUser recipient) {
        authenticationService.getSessionForUser(recipient).ifPresentOrElse(
                r -> eventBus.publishEvent(new ServerMessageWrapper(message, r)),
                () -> log.warn("Could not get session for user")
        );
    }

    /**
     * This method sends the given message to the given recipient.
     *
     * @param message   the message to send
     * @param recipient the session, which should receive the message
     */
    protected void sendTo(IServerMessage message, ISession recipient) {
        eventBus.publishEvent(new ServerMessageWrapper(message, recipient));
    }

    /**
     * This method sends the given message to the given recipient.
     *
     * @param message   the message to send
     * @param recipient the user, which should receive the message
     */
    protected void requestAndSendTo(IServerMessage message, ServerUser recipient) {
        Collection<IServerMessage> additionalMessages = secondaryRequestHandler.handle(message, recipient);

        authenticationService.getSessionForUser(recipient).ifPresentOrElse(
                r -> eventBus.publishEvent(new ServerMessageWrapper(message, r, additionalMessages)),
                () -> log.warn("Could not get session for user")
        );
    }

}
