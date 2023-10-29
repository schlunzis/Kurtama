package de.schlunzis.client.service.impl;

import de.schlunzis.client.service.ISessionService;
import de.schlunzis.common.User;
import de.schlunzis.common.messages.authentication.LoginSuccessfulResponse;
import de.schlunzis.common.messages.authentication.LogoutSuccessfulResponse;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A component to save information about the current session. Like the user the client is logged in as.
 */
@Getter
@Service
public class SessionService implements ISessionService {

    private Optional<User> currentUser = Optional.empty();

    @EventListener
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse lsr) {
        currentUser = Optional.of(lsr.getUser());
    }

    @EventListener
    public void onLogoutSuccessfulResponse(LogoutSuccessfulResponse lsr) {
        currentUser = Optional.empty();
    }

}