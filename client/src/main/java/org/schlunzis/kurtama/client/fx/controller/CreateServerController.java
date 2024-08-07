package org.schlunzis.kurtama.client.fx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.server.Server;
import org.schlunzis.kurtama.client.server.ServerType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateServerController {

    private final Server server;

    @FXML
    private ComboBox<ServerType> typeSelector;
    @FXML
    private TextField portField;

    @FXML
    private void initialize() {
        typeSelector.setItems(FXCollections.observableList(Arrays.asList(ServerType.values())));
    }

    @FXML
    private void handleTestRequirements() {
        server.setServerType(typeSelector.getValue());
        log.info("Can system run server? {}", server.testRequirements());
    }

    @FXML
    private void handleRun() {
        int port = Integer.parseInt(portField.getText()); // TODO exception handling
        server.run(port);
    }

}
