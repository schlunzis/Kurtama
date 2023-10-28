package de.schlunzis.client.controller;

import com.google.common.eventbus.EventBus;
import de.schlunzis.common.messages.authentication.LogoutRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainMenuController {

    private final ApplicationEventPublisher eventBus;

    @FXML
    private ListView lobbiesListView; // TODO implement


    @FXML
    private void logout() {
        log.info("Logout button clicked");
        eventBus.post(new LogoutRequest());
    }

    @FXML
    private void settings(ActionEvent actionEvent) {
        // TODO implement
    }

    @FXML
    private void joinLobby(ActionEvent actionEvent) {
        // TODO implement
    }

    @FXML
    private void createLobby(ActionEvent actionEvent) {
        // TODO implement
    }

}
