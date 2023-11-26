package org.schlunzis.kurtama.client.fx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.messages.lobby.client.LeaveLobbyRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("lobby.fxml")
@Component
@RequiredArgsConstructor
public class LobbyController {

    private final ApplicationEventPublisher eventBus;
    private final ISessionService sessionService;

    @FXML
    private ListView<String> userListView;

    public void leaveLobby(ActionEvent actionEvent) {
        sessionService.getCurrentLobby().ifPresent(lobby ->
                eventBus.publishEvent(new LeaveLobbyRequest(lobby.getId()))
        );
    }

    @FXML
    public void initialize() {
        userListView.setItems(sessionService.getLobbyUsersList());
    }


}
