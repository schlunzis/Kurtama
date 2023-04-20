package de.schlunzis.server.net;

import com.google.common.eventbus.EventBus;
import de.schlunzis.common.messages.ClientMessage;
import de.schlunzis.common.messages.authentication.LoginRequest;
import de.schlunzis.server.auth.AuthenticationService;
import de.schlunzis.server.auth.LoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClientMessageDispatcher {

    private final EventBus eventBus;

    private final AuthenticationService authenticationService;

    public ClientMessageDispatcher(EventBus eventBus, AuthenticationService authenticationService) {
        this.eventBus = eventBus;
        this.authenticationService = authenticationService;
    }

    public void dispatch(ClientMessage clientMessage, Session session) {
        log.info("going to dispatch message {}", clientMessage);
        if (clientMessage instanceof LoginRequest lir) {
            log.info("Received LoginRequest");
            eventBus.post(new LoginEvent(lir, session));
        } else {
            log.info("Received ClientMessage");
            if (authenticationService.isLoggedIn(session))
                eventBus.post(clientMessage);
        }
    }
}
