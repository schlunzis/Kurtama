package de.schlunzis.server.auth;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.common.messages.authentication.LoginFailedResponse;
import de.schlunzis.common.messages.authentication.LoginRequest;
import de.schlunzis.common.messages.authentication.LoginSuccessfulResponse;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import de.schlunzis.server.net.MessageWrapper;
import de.schlunzis.server.net.Session;
import de.schlunzis.server.user.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Component
public class AuthenticationService {

    private final EventBus eventBus;

    private final UserSessionMap userSessionMap;

    private final UserStore userStore;

    public AuthenticationService(EventBus eventBus, UserSessionMap userSessionMap, UserStore userStore) {
        this.eventBus = eventBus;
        this.userSessionMap = userSessionMap;
        this.userStore = userStore;
        eventBus.register(this);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        log.info("Received LoginEvent. Going to authenticate user");
        LoginRequest loginRequest = loginEvent.getLoginRequest();

        userStore.getUser(loginRequest.getEmail()).ifPresentOrElse(user -> {
            if (user.getPassword().equals(loginRequest.getPassword())) {
                userSessionMap.put(user, loginEvent.getSession());
                log.info("User {} logged in", user.getEmail());
                eventBus.post(new MessageWrapper(new LoginSuccessfulResponse(user.toDTO()), loginEvent.getSession()));
                eventBus.post(new MessageWrapper(new ServerChatMessage(UUID.randomUUID(), "SERVER", "Welcome to the chat!"), getAllLoggedInSessions()));
            } else {
                log.info("User {} tried to log in with wrong password", user.getEmail());
                eventBus.post(new LoginFailedResponse());
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
