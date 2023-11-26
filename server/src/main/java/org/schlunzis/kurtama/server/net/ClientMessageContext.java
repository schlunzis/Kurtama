package org.schlunzis.kurtama.server.net;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.service.SecondaryRequestContext;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.Collection;
import java.util.Optional;

/**
 * thanks to <a href="https://stackoverflow.com/questions/71452445/eventlistener-for-generic-events-with-spring">StackOverflow</a>
 * <p>
 * A wrapper for messages to be sent via the {@link ApplicationEventPublisher}. This wrapper
 * contains the message, the session and a user.
 * <p>
 * The message is the Message received from the client. The session is the session associated with the channel the
 * message was received from. The user is the user associated with the session, if the user is logged in. If the user
 * is not logged in, this user will be null. However, it can be assumed, that the user is not null, if the message is
 * neither a {@link LoginRequest} nor a {@link RegisterRequest}
 */
@Slf4j
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ClientMessageContext<T extends IClientMessage> implements ResolvableTypeProvider {

    private final T clientMessage;
    private final ISession session;
    private final ServerUser user;

    @Getter(AccessLevel.NONE)
    private final ResponseAssembler responseAssembler;

    @Getter(AccessLevel.NONE)
    private final AuthenticationService authenticationService;
    @Getter(AccessLevel.NONE)
    private final ApplicationEventPublisher eventBus;

    public void respond(IServerMessage message) {
        responseAssembler.setMainResponse(new ServerMessageWrapper(message, session));
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

    public void close() {
        eventBus.publishEvent(new ServerMessageWrappers(responseAssembler.assemble()));
    }

    public void closeWithReRequest() {
        Optional<ServerMessageWrapper> mainResponse = responseAssembler.getMainResponse();
        if (mainResponse.isPresent()) {
            SecondaryRequestContext<T, IServerMessage> secondaryRequestContext =
                    new SecondaryRequestContext<>(mainResponse.get().getServerMessage(), this,
                            responseAssembler, authenticationService, eventBus);
            eventBus.publishEvent(secondaryRequestContext);
        }
        close();
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forInstance(this.clientMessage)
        );
    }

}
