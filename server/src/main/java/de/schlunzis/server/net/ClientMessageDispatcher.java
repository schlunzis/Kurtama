package de.schlunzis.server.net;

import de.schlunzis.common.messages.IClientMessage;
import de.schlunzis.common.messages.authentication.login.LoginRequest;
import de.schlunzis.common.messages.authentication.register.RegisterRequest;
import de.schlunzis.server.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientMessageDispatcher {

    private final AuthenticationService authenticationService;

    private final ApplicationEventPublisher eventBus;


    public void dispatch(IClientMessage clientMessage, ISession session) {
        log.info("going to dispatch message {}", clientMessage);
        if (clientMessage instanceof LoginRequest lir) {
            eventBus.publishEvent(new ClientMessageWrapper<>(lir, session));
        } else if (clientMessage instanceof RegisterRequest rr) {
            eventBus.publishEvent(new ClientMessageWrapper<>(rr, session));
        } else {
            log.info("Received ClientMessage");
            if (authenticationService.isLoggedIn(session))
                eventBus.publishEvent(new ClientMessageWrapper<>(clientMessage, session));
        }
    }
}
