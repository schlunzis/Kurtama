package org.schlunzis.kurtama.client.fx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.fx.dialog.CreateLobbyDialogResult;
import org.schlunzis.kurtama.client.fx.dialog.DialogFactory;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.CreateLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.JoinLobbyRequest;
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
    private final ISessionService sessionService;

    @FXML
    private ListView<LobbyInfo> lobbiesListView;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private void initialize() {
        lobbiesListView.setItems(sessionService.getLobbyList());
        lobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lobbiesListView.setCellFactory(lobbyInfoListView -> new ListCell<>() {
            @Override
            protected void updateItem(LobbyInfo lobbyInfo, boolean empty) {
                super.updateItem(lobbyInfo, empty);
                if (empty || lobbyInfo == null) {
                    setText(null);
                } else {
                    setText(lobbyInfo.lobbyName() + " (" + lobbyInfo.users() + ")");
                }
            }
        });
        lobbiesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                joinLobbyButton.setDisable(newValue == null)
        );
        lobbiesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2)
                joinLobby();
        });
        joinLobbyButton.setDisable(true);
    }

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
    private void joinLobby() {
        LobbyInfo li = lobbiesListView.getSelectionModel().getSelectedItem();
        if (li == null)
            return;
        eventBus.publishEvent(new JoinLobbyRequest(li.lobbyID()));
    }

    @FXML
    private void createLobby(ActionEvent actionEvent) {
        Optional<Dialog<CreateLobbyDialogResult>> dialog = dialogFactory.createCreateLobbyDialog();
        dialog.flatMap(Dialog::showAndWait).ifPresent(result ->
                eventBus.publishEvent(new CreateLobbyRequest(result.getName()))
        );
    }

}
