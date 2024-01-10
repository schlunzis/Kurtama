package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.internal.ForcedLogoutEvent;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.schlunzis.kurtama.server.service.ResponseAssembler;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientMessageDispatcher {

    private final AuthenticationService authenticationService;

    private final ApplicationEventPublisher eventBus;

    public void dispatch(IClientMessage clientMessage, ISession session) {
        log.info("going to dispatch message {}", clientMessage);
        ResponseAssembler responseAssembler = new ResponseAssembler(clientMessage);
        if (clientMessage instanceof LoginRequest lir) {
            eventBus.publishEvent(new ClientMessageContext<>(lir, session, null, responseAssembler, eventBus));
        } else if (clientMessage instanceof RegisterRequest rr) {
            eventBus.publishEvent(new ClientMessageContext<>(rr, session, null, responseAssembler, eventBus));
        } else {
            Optional<ServerUser> user = authenticationService.getUserForSession(session);
            user.ifPresentOrElse(
                    u -> {
                        log.info("Received authenticated ClientMessage");
                        eventBus.publishEvent(
                                new ClientMessageContext<>(clientMessage, session, u, responseAssembler, eventBus)
                        );
                    },
                    () -> log.info("Received unauthenticated ClientMessage")
            );
        }
    }

    public void newClient(ISession session) {
        // some things could be done here
    }

    public void clientDisconnected(ISession session) {
        eventBus.publishEvent(new ForcedLogoutEvent(session));
    }

}
