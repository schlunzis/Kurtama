package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.service.ILobbyService;
import org.schlunzis.kurtama.client.service.impl.SessionService;
import org.schlunzis.kurtama.common.IUser;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LobbyController {

    private final ILobbyService lobbyService;
    private final SessionService sessionService;

    @FXML
    private ListView<IUser> userListView;

    @FXML
    private Button startGameButton;

    public void leaveLobby() {
        lobbyService.leaveLobby();
    }

    @FXML
    public void initialize() {
        userListView.setItems(lobbyService.getLobbyUsersList());

        userListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(IUser user, boolean empty) {
                super.updateItem(user, empty);
                lobbyService.getCurrentLobby().ifPresentOrElse(lobby -> {
                    IUser owner = lobby.getOwner();
                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getUsername() + (user.equals(owner) ? " (Owner)" : ""));
                    }
                }, () -> log.warn("No lobby found"));
            }
        });

        IUser currentUser = sessionService.getCurrentUser().orElseThrow(() -> new IllegalStateException("No user found"));
        lobbyService.getCurrentLobby().ifPresent(lobby -> startGameButton.setVisible(lobby.getOwner().equals(currentUser)));
    }

    @FXML
    private void startGame() {
        lobbyService.startGame();
    }

}
