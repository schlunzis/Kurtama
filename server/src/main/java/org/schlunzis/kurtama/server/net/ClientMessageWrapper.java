package org.schlunzis.kurtama.server.net;

import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * thanks to <a href="https://stackoverflow.com/questions/71452445/eventlistener-for-generic-events-with-spring">StackOverflow</a>
 * <p>
 * A wrapper for messages to be sent via the {@link org.springframework.context.ApplicationEventPublisher}. This wrapper
 * contains the message, the session and a user.
 * <p>
 * The message is the Message received from the client. The session is the session associated with the channel the
 * message was received from. The user is the user associated with the session, if the user is logged in. If the user
 * is not logged in, this user will be null. However, it can be assumed, that the user is not null, if the message is
 * neither a {@link LoginRequest} nor a {@link RegisterRequest}
 *
 * @param clientMessage the message received by the server
 * @param session       the session belonging to the channel the message was received from
 * @param user          the user the session belongs to, if the user is logged in, otherwise null.
 * @param <T>           the type of the message
 */
public record ClientMessageWrapper<T extends IClientMessage>(T clientMessage,
                                                             ISession session,
                                                             ServerUser user) implements ResolvableTypeProvider {

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forInstance(this.clientMessage)
        );
    }
}
