package de.schlunzis.server.auth;

import de.schlunzis.common.messages.authentication.login.LoginFailedResponse;
import de.schlunzis.common.messages.authentication.login.LoginRequest;
import de.schlunzis.common.messages.authentication.login.LoginSuccessfulResponse;
import de.schlunzis.common.messages.authentication.logout.LogoutRequest;
import de.schlunzis.common.messages.authentication.logout.LogoutSuccessfulResponse;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import de.schlunzis.server.net.ClientMessageWrapper;
import de.schlunzis.server.net.ISession;
import de.schlunzis.server.net.ServerMessageWrapper;
import de.schlunzis.server.user.IUserStore;
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

    private final IUserStore userStore;

    @EventListener
    public void onLoginEvent(ClientMessageWrapper<LoginRequest> cmw) {
        log.info("Received LoginEvent. Going to authenticate user");
        LoginRequest loginRequest = cmw.clientMessage();

        userStore.getUser(loginRequest.getEmail()).ifPresentOrElse(user -> {
            if (user.getPassword().equals(loginRequest.getPassword())) {
                userSessionMap.put(user, cmw.session());
                log.info("User {} logged in", user.getEmail());
                eventBus.publishEvent(new ServerMessageWrapper(new LoginSuccessfulResponse(user.toDTO()), cmw.session()));
                eventBus.publishEvent(new ServerMessageWrapper(new ServerChatMessage(UUID.randomUUID(), "SERVER", null, "Welcome to the chat!"), getAllLoggedInSessions()));
            } else {
                log.info("User {} tried to log in with wrong password", user.getEmail());
                eventBus.publishEvent(new LoginFailedResponse());
            }
        }, () -> log.info("User {} not found", loginRequest.getEmail()));

    }

    @EventListener
    public void onLogoutRequest(ClientMessageWrapper<LogoutRequest> cmw) {
        userSessionMap.remove(cmw.session());
        eventBus.publishEvent(new ServerMessageWrapper(new LogoutSuccessfulResponse(), cmw.session()));
        log.info("Client with session {} logged out", cmw.session());
    }

    public boolean isLoggedIn(ISession session) {
        return userSessionMap.contains(session);
    }

    public Collection<ISession> getAllLoggedInSessions() {
        return userSessionMap.getAllSessions();
    }


}
