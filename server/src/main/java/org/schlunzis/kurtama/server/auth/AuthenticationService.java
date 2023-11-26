package org.schlunzis.kurtama.server.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutRequest;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.server.lobby.LobbyStore;
import org.schlunzis.kurtama.server.net.ClientMessageContext;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * This class handles all login-, logout- and registration events. It also provides information about whether a user is
 * logged in. If a user is logged in, a session for that user can be retrieved.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final ApplicationEventPublisher eventBus;
    private final UserSessionMap userSessionMap;
    private final IUserStore userStore;
    private final LobbyStore lobbyStore;
    private final PasswordEncoder passwordEncoder;

    // ################################################
    // Event Listeners
    // ################################################

    @EventListener
    public void onLoginEvent(ClientMessageContext<LoginRequest> cmw) {
        log.info("Received LoginEvent. Going to authenticate user");
        LoginRequest loginRequest = cmw.getClientMessage();

        userStore.getUser(loginRequest.getEmail()).ifPresentOrElse(user -> {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                userSessionMap.put(user.toServerUser(), cmw.getSession());
                log.info("User {} logged in", user.getEmail());
                Collection<LobbyInfo> lobbyInfos = lobbyStore.getAll().stream().map(l -> new LobbyInfo(l.getId(), l.getName(), l.getUsers().size())).toList();
                eventBus.publishEvent(new ServerMessageWrapper(new LoginSuccessfulResponse(user.toServerUser().toDTO(), user.getEmail(), lobbyInfos), cmw.getSession()));
                eventBus.publishEvent(new ServerMessageWrapper(new ServerChatMessage(new UUID(0, 0), "SERVER", null, "Welcome to the chat!"), cmw.getSession()));
            } else {
                log.info("User {} tried to log in with wrong password", user.getEmail());
                eventBus.publishEvent(new LoginFailedResponse());
            }
        }, () -> log.info("User {} not found", loginRequest.getEmail()));

    }

    @EventListener
    public void onLogoutRequest(ClientMessageContext<LogoutRequest> cmw) {
        userSessionMap.remove(cmw.getSession());
        eventBus.publishEvent(new ServerMessageWrapper(new LogoutSuccessfulResponse(), cmw.getSession()));
        log.info("Client with session {} logged out", cmw.getSession());
    }

    @EventListener
    public void onRegisterEvent(ClientMessageContext<RegisterRequest> cmw) {
        RegisterRequest rr = cmw.getClientMessage();
        String email = rr.getEmail();
        String username = rr.getUsername();
        String password = rr.getPassword();
        try {
            userStore.createUser(new DBUser(email, username, password));
            eventBus.publishEvent(new ServerMessageWrapper(new RegisterSuccessfulResponse(), cmw.getSession()));
        } catch (IllegalArgumentException iae) {
            log.info("User with email {} already exists", email);
            eventBus.publishEvent(new ServerMessageWrapper(new RegisterFailedResponse(), cmw.getSession()));
        }
    }

    // ################################################
    // Other methods
    // ################################################

    public boolean isLoggedIn(ISession session) {
        return userSessionMap.contains(session);
    }

    public Collection<ISession> getAllLoggedInSessions() {
        return userSessionMap.getAllSessions();
    }

    public Optional<ServerUser> getUserForSession(ISession session) {
        return userSessionMap.get(session);
    }

    @Override
    public Collection<ISession> getSessionsForUsers(Collection<ServerUser> users) {
        return userSessionMap.getFor(users);
    }

    @Override
    public Optional<ISession> getSessionForUser(ServerUser user) {
        return userSessionMap.get(user);
    }

}
