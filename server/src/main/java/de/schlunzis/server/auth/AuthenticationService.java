package de.schlunzis.server.auth;

import de.schlunzis.common.messages.ClientMessage;
import de.schlunzis.common.messages.authentication.LoginFailedResponse;
import de.schlunzis.common.messages.authentication.LoginRequest;
import de.schlunzis.common.messages.authentication.LoginSuccessfulResponse;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import de.schlunzis.server.net.ClientMessageWrapper;
import de.schlunzis.server.net.ServerMessageWrapper;
import de.schlunzis.server.net.Session;
import de.schlunzis.server.user.UserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {


    private final ApplicationEventPublisher eventBus;

    private final UserSessionMap userSessionMap;

    private final UserStore userStore;

    @EventListener
    public void onLoginEvent(ClientMessageWrapper<ClientMessage> cmw) {
        log.info("Received LoginEvent. Going to authenticate user");
        LoginRequest loginRequest = (LoginRequest) cmw.clientMessage();

        userStore.getUser(loginRequest.getEmail()).ifPresentOrElse(user -> {
            if (user.getPassword().equals(loginRequest.getPassword())) {
                userSessionMap.put(user, cmw.session());
                log.info("User {} logged in", user.getEmail());
                eventBus.publishEvent(new ServerMessageWrapper(new LoginSuccessfulResponse(user.toDTO()), cmw.session()));
                eventBus.publishEvent(new ServerMessageWrapper(new ServerChatMessage(UUID.randomUUID(), "SERVER", "Welcome to the chat!"), getAllLoggedInSessions()));
            } else {
                log.info("User {} tried to log in with wrong password", user.getEmail());
                eventBus.publishEvent(new LoginFailedResponse());
            }
        }, () -> log.info("User {} not found", loginRequest.getEmail()));

    }

    public boolean isLoggedIn(Session session) {
        return userSessionMap.contains(session);
    }

    public Collection<Session> getAllLoggedInSessions() {
        return userSessionMap.getAllSessions();
    }


}
