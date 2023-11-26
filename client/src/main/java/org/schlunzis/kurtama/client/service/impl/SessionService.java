package org.schlunzis.kurtama.client.service.impl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.common.messages.lobby.server.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * A component to save information about the current session. Like the user the client is logged in as.
 */
@Getter
@Service
public class SessionService implements ISessionService {

    private static final UUID GLOBAL_CHAT_ID = new UUID(0, 0);

    private final ObservableList<LobbyInfo> lobbyList = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<String> chatMessages = FXCollections.observableList(new ArrayList<>());
    private final ObservableList<String> lobbyUsersList = FXCollections.observableList(new ArrayList<>());
    private UUID currentChatID = GLOBAL_CHAT_ID;
    private Optional<IUser> currentUser = Optional.empty();
    private Optional<ILobby> currentLobby = Optional.empty();

    @EventListener
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse lsr) {
        currentUser = Optional.of(lsr.getUser());
        lobbyList.setAll(lsr.getLobbyInfos());
    }

    @EventListener
    public void onLogoutSuccessfulResponse(LogoutSuccessfulResponse lsr) {
        currentUser = Optional.empty();
        currentChatID = GLOBAL_CHAT_ID;
    }

    @EventListener
    public void onLobbyCreatedSuccessfullyResponse(LobbyCreatedSuccessfullyResponse lcsr) {
        currentLobby = Optional.of(lcsr.lobby());
        currentChatID = lcsr.lobby().getChatID();
        Platform.runLater(() -> {
            lobbyUsersList.clear();
            lcsr.lobby().getUsers().forEach(u -> lobbyUsersList.add(u.getUsername()));
        });
    }

    @EventListener
    public void onJoinLobbySuccessfullyResponse(JoinLobbySuccessfullyResponse jlsr) {
        currentLobby = Optional.of(jlsr.lobby());
        currentChatID = jlsr.lobby().getChatID();
        Platform.runLater(() -> {
            lobbyUsersList.clear();
            jlsr.lobby().getUsers().forEach(u -> lobbyUsersList.add(u.getUsername()));
        });
    }

    @EventListener
    public void onLeaveLobbySuccessfullyResponse(LeaveLobbySuccessfullyResponse llsr) {
        currentLobby = Optional.empty();
        currentChatID = GLOBAL_CHAT_ID;
    }

    @EventListener
    void onUserLeftLobbyMessage(UserLeftLobbyMessage ullm) {
        Platform.runLater(() -> lobbyUsersList.remove(ullm.leavingLobbyMember().getUsername()));
    }

    @EventListener
    void onUserJoinedLobbyMessage(UserJoinedLobbyMessage ujlm) {
        Platform.runLater(() -> lobbyUsersList.add(ujlm.joiningLobbyMember().getUsername()));
    }

    @EventListener
    public void onLobbyListInfoMessage(LobbyListInfoMessage llim) {
        Platform.runLater(() ->
                lobbyList.setAll(llim.lobbies())
        );
    }

    @EventListener
    public void onServerChatMessage(ServerChatMessage message) {
        if (message.getChatID().equals(currentChatID)) {
            Platform.runLater(() -> chatMessages.add(message.getNickname() + ": " + message.getMessage()));
        }
    }

}
