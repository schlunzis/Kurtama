package org.schlunzis.kurtama.client.fx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.events.ClientClosingEvent;
import org.schlunzis.kurtama.client.server.Server;
import org.schlunzis.kurtama.client.server.ServerFactory;
import org.schlunzis.kurtama.client.server.ServerType;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateServerController {

    private final ServerFactory serverFactory;
    private Server server;

    @FXML
    private ComboBox<ServerType> typeSelector;
    @FXML
    private TextField portField;
    @FXML
    private TextField pathField;

    @FXML
    private void initialize() {
        typeSelector.setItems(FXCollections.observableList(Arrays.asList(ServerType.values())));
        typeSelector.setValue(ServerType.JAR);
        typeSelector.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            log.info("Selected server type: {}", newValue);
            server = serverFactory.getServer(typeSelector.getValue());
        });
        pathField.setText(System.getProperty("user.home") + File.separator + ".kurtama"); // TODO get from settings
    }

    @FXML
    private void handleTestRequirements() {
        if (server != null) {
            server.stop();
            log.info("Can system run server? {}", server.testRequirements());
        } else {
            log.error("No server selected");
        }
    }

    @FXML
    private void handleRun() {
        int port = Integer.parseInt(portField.getText()); // TODO exception handling
        server.run(port, pathField.getText());
    }

    @EventListener
    public void onClientClosingEvent(ClientClosingEvent ignored) {
        if (server != null)
            server.stop();
    }

    @FXML
    public void handleSelectPath() {
        File initialDirectory = new File(pathField.getText());
        if (!initialDirectory.exists()) {
            initialDirectory.mkdirs();
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(initialDirectory);
        directoryChooser.setTitle("Select server path");
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

}
