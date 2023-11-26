package org.schlunzis.kurtama.server.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.ResponseAssembler;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.net.ServerMessageWrappers;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.List;
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
@EqualsAndHashCode(callSuper = true)
public class ClientMessageContext<T extends IClientMessage> extends AbstractMessageContext implements ResolvableTypeProvider {

    private final T clientMessage;

    public ClientMessageContext(T clientMessage, ISession session, ServerUser user, ResponseAssembler responseAssembler, AuthenticationService authenticationService, ApplicationEventPublisher eventBus) {
        super(responseAssembler, authenticationService, eventBus, session, user);
        this.clientMessage = clientMessage;
    }

    public void respond(IServerMessage message) {
        responseAssembler.setMainResponse(new ServerMessageWrapper(message, session));
    }

    public void close() {
        List<ServerMessageWrapper> wrappers = responseAssembler.assemble();
        if (!wrappers.isEmpty())
            eventBus.publishEvent(new ServerMessageWrappers(wrappers));
    }

    public void closeWithReRequest() {
        Optional<ServerMessageWrapper> mainResponse = responseAssembler.getMainResponse();
        if (mainResponse.isPresent()) {
            SecondaryRequestContext<T, IServerMessage> secondaryRequestContext =
                    new SecondaryRequestContext<>(mainResponse.get().getServerMessage(), this,
                            responseAssembler, authenticationService, eventBus);
            log.info("sending secondary request {}", secondaryRequestContext);
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
