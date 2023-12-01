package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
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
    private void leaveLobby() {
        lobbyService.leaveLobby();
    }

}
