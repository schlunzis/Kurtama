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
import org.schlunzis.kurtama.server.internal.ForcedLogoutEvent;
import org.schlunzis.kurtama.server.lobby.LobbyStore;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.schlunzis.kurtama.server.service.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

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
    public void onLoginEvent(ClientMessageContext<LoginRequest> cmc) {
        log.info("Received LoginEvent. Going to authenticate user");
        LoginRequest loginRequest = cmc.getClientMessage();

        userStore.getUser(loginRequest.getEmail()).ifPresentOrElse(user -> {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {

                userSessionMap.get(user.toServerUser()).ifPresent(oldSession -> {
                    log.info("User {} already logged in. Going to log out old session {}", user.getEmail(), oldSession);
                    logout(oldSession);
                });

                userSessionMap.put(user.toServerUser(), cmc.getSession());
                log.info("User {} logged in", user.getEmail());
                Collection<LobbyInfo> lobbyInfos = lobbyStore.getAll().stream().map(l -> new LobbyInfo(l.getId(), l.getName(), l.getUsers().size())).toList();
                cmc.respond(new LoginSuccessfulResponse(user.toServerUser().toDTO(), user.getEmail(), lobbyInfos));
            } else {
                log.info("User {} tried to log in with wrong password", user.getEmail());
                cmc.respond(new LoginFailedResponse());
            }
        }, () -> log.info("User {} not found", loginRequest.getEmail()));
        cmc.closeWithReRequest();
    }

    @EventListener
    public void onLogoutRequest(ClientMessageContext<LogoutRequest> cmc) {
        logout(cmc.getSession());
    }

    @EventListener
    public void onRegisterEvent(ClientMessageContext<RegisterRequest> cmc) {
        RegisterRequest rr = cmc.getClientMessage();
        String email = rr.getEmail();
        String username = rr.getUsername();
        String password = rr.getPassword();
        try {
            userStore.createUser(new DBUser(email, username, password));
            cmc.respond(new RegisterSuccessfulResponse());
        } catch (IllegalArgumentException iae) {
            log.info("User with email {} already exists", email);
            cmc.respond(new RegisterFailedResponse());
        }
        cmc.close();
    }

    @EventListener
    public void onLogoutRequest(ForcedLogoutEvent forcedLogoutEvent) {
        logout(forcedLogoutEvent.session());
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

    // not using the ClientMessageContext due to the ForcedLogoutEvent. might be changed in the future
    private void logout(ISession session) {
        userSessionMap.remove(session);
        eventBus.publishEvent(new ServerMessageWrapper(new LogoutSuccessfulResponse(), user));
        log.info("Client with session {} logged out", session);
    }

}
