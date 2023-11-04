package org.schlunzis.kurtama.server.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutRequest;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterSuccessfullResponse;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.server.net.ClientMessageWrapper;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void onLoginEvent(ClientMessageWrapper<LoginRequest> cmw) {
        log.info("Received LoginEvent. Going to authenticate user");
        LoginRequest loginRequest = cmw.clientMessage();

        userStore.getUser(loginRequest.getEmail()).ifPresentOrElse(user -> {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                userSessionMap.put(user.toServerUser(), cmw.session());
                log.info("User {} logged in", user.getEmail());
                eventBus.publishEvent(new ServerMessageWrapper(new LoginSuccessfulResponse(user.toServerUser().toDTO(), user.getEmail()), cmw.session()));
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

    @EventListener
    public void onRegisterEvent(ClientMessageWrapper<RegisterRequest> cmw) {
        RegisterRequest rr = cmw.clientMessage();
        String email = rr.getEmail();
        String username = rr.getUsername();
        String password = rr.getPassword();
        try {
            userStore.createUser(new DBUser(email, username, password));
            eventBus.publishEvent(new ServerMessageWrapper(new RegisterSuccessfullResponse(), cmw.session()));
        } catch (IllegalArgumentException iae) {
            log.info("User with email {} already exists", email);
            eventBus.publishEvent(new ServerMessageWrapper(new RegisterFailedResponse(), cmw.session()));
        }
    }

    public boolean isLoggedIn(ISession session) {
        return userSessionMap.contains(session);
    }

    public Collection<ISession> getAllLoggedInSessions() {
        return userSessionMap.getAllSessions();
    }


}
