package org.schlunzis.kurtama.client.fx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.fx.dialog.CreateLobbyDialogResult;
import org.schlunzis.kurtama.client.fx.dialog.DialogFactory;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.CreateLobbyRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@FxmlView("main.fxml")
@Component
@RequiredArgsConstructor
public class MainMenuController {

    private final ApplicationEventPublisher eventBus;
    private final DialogFactory dialogFactory;

    @FXML
    private ListView lobbiesListView; // TODO implement


    @FXML
    private void logout() {
        log.info("Logout button clicked");
        eventBus.publishEvent(new LogoutRequest());
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
        Optional<Dialog<CreateLobbyDialogResult>> dialog = dialogFactory.createCreateLobbyDialog();
        dialog.flatMap(Dialog::showAndWait).ifPresent(result ->
                eventBus.publishEvent(new CreateLobbyRequest(result.getName()))
        );
    }

}
