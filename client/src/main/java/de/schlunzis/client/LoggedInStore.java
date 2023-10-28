package de.schlunzis.client;

import de.schlunzis.common.User;
import de.schlunzis.common.messages.authentication.LoginSuccessfulResponse;
import de.schlunzis.common.messages.authentication.LogoutSuccessfulResponse;
import lombok.Getter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * A component to save information about the current session. Like the user the client is logged in as.
 */
@Getter
@Component
public class LoggedInStore {

    private Optional<User> loggedInUser = Optional.empty();

    @EventListener
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse lsr) {
        loggedInUser = Optional.of(lsr.getUser());
    }

    @EventListener
    public void onLogoutSuccessfulResponse(LogoutSuccessfulResponse lsr) {
        loggedInUser = Optional.empty();
    }

}
