package org.schlunzis.kurtama.client.service.impl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.service.ILobbyService;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.messages.game.client.StartGameRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.LeaveLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.server.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class LobbyService implements ILobbyService {

    private final ApplicationEventPublisher eventBus;

    private final ObservableList<String> lobbyUsersList = FXCollections.observableList(new ArrayList<>());
    private Optional<ILobby> currentLobby = Optional.empty();

    @EventListener
    public void onLobbyCreatedSuccessfullyResponse(LobbyCreatedSuccessfullyResponse csr) {
        currentLobby = Optional.of(csr.lobby());
        Platform.runLater(() -> {
                    lobbyUsersList.clear();
                    csr.lobby().getUsers().forEach(u -> lobbyUsersList.add(u.getUsername()));
                }
        );
    }

    @EventListener
    public void onJoinLobbySuccessfullyResponse(JoinLobbySuccessfullyResponse jsr) {
        currentLobby = Optional.of(jsr.lobby());
        Platform.runLater(() -> {
                    lobbyUsersList.clear();
                    jsr.lobby().getUsers().forEach(u -> lobbyUsersList.add(u.getUsername()));
                }
        );
    }

    @EventListener
    public void onLeaveLobbySuccessfullyResponse(LeaveLobbySuccessfullyResponse ignored) {
        currentLobby = Optional.empty();
    }

    public void leaveLobby() {
        currentLobby.ifPresent(lobby ->
                eventBus.publishEvent(new LeaveLobbyRequest(lobby.getId()))
        );
    }

    @Override
    public void startGame() {
        currentLobby.ifPresent(lobby ->
                eventBus.publishEvent(new StartGameRequest(new GameSettings(6, 8)))
        );
    }

    @EventListener
    void onUserLeftLobbyMessage(UserLeftLobbyMessage ullm) {
        Platform.runLater(() -> lobbyUsersList.remove(ullm.leavingLobbyMember().getUsername()));
    }

    @EventListener
    void onUserJoinedLobbyMessage(UserJoinedLobbyMessage ujlm) {
        Platform.runLater(() -> lobbyUsersList.add(ujlm.joiningLobbyMember().getUsername()));
    }


}
