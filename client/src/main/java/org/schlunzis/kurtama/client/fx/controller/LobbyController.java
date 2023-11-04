package org.schlunzis.kurtama.client.fx.controller;

import javafx.event.ActionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("lobby.fxml")
@Component
@RequiredArgsConstructor
public class LobbyController {

    private final ApplicationEventPublisher eventBus;

    public void leaveLobby(ActionEvent actionEvent) {

    }

}
