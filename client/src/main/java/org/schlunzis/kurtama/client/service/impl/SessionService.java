package org.schlunzis.kurtama.client.service.impl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LobbyListInfoMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A component to save information about the current session. Like the user the client is logged in as.
 */
@Getter
@Service
public class SessionService implements ISessionService {

    private final ObservableList<LobbyInfo> lobbyList = FXCollections.observableList(new ArrayList<>());
    private Optional<IUser> currentUser = Optional.empty();

    @EventListener
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse lsr) {
        currentUser = Optional.of(lsr.getUser());
        Platform.runLater(() ->
                lobbyList.setAll(lsr.getLobbyInfos())
        );
    }

    @EventListener
    public void onLogoutSuccessfulResponse(LogoutSuccessfulResponse ignored) {
        currentUser = Optional.empty();
    }

    @EventListener
    public void onLobbyListInfoMessage(LobbyListInfoMessage lim) {
        Platform.runLater(() ->
                lobbyList.setAll(lim.lobbies())
        );
    }

}
