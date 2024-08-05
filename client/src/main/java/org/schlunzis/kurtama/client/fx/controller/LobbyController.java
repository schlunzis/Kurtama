package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.service.ILobbyService;
import org.schlunzis.kurtama.common.IUser;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LobbyController {

    private final ILobbyService lobbyService;

    @FXML
    private ListView<IUser> userListView;


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
    }


}
