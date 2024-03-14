package org.schlunzis.kurtama.client.fx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.service.ILobbyService;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("lobby.fxml")
@Component
@RequiredArgsConstructor
public class LobbyController {

    private final ILobbyService lobbyService;

    @FXML
    private ListView<String> userListView;


    public void leaveLobby() {
        lobbyService.leaveLobby();
    }

    @FXML
    public void initialize() {
        userListView.setItems(lobbyService.getLobbyUsersList());
    }

    public void startGame(ActionEvent actionEvent) {
        lobbyService.startGame();
    }

}
