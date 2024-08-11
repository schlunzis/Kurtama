package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.events.ClientClosingEvent;
import org.schlunzis.kurtama.client.fx.view.StatusView;
import org.schlunzis.kurtama.client.server.*;
import org.schlunzis.kurtama.client.settings.IUserSettings;
import org.schlunzis.kurtama.client.settings.Setting;
import org.schlunzis.kurtama.client.util.I18n;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateServerController {

    private final ServerFactory serverFactory;
    private final LogSink logSink;
    private final IUserSettings userSettings;
    private final I18n i18n;
    private Server server;

    @FXML
    private ComboBox<ServerType> typeSelector;
    @FXML
    private StatusView requirementsStatusView;
    @FXML
    private TextField portField;
    @FXML
    private TextField pathField;
    @FXML
    private StatusView serverStatusView;
    @FXML
    private TextArea logArea;

    @FXML
    private void initialize() {
        requirementsStatusView.setI18n(i18n);
        serverStatusView.setI18n(i18n);
        pathField.setText(userSettings.getString(Setting.SERVER_PATH));
        portField.setText(String.valueOf(userSettings.getInt(Setting.PORT)));
        portField.textProperty().addListener((_, _, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portField.setText(newValue.replaceAll("\\D", ""));
            }
        });
        typeSelector.setItems(FXCollections.observableList(Arrays.asList(ServerType.values())));
        typeSelector.getSelectionModel().select(userSettings.getServerType(Setting.SERVER_TYPE));
        server = createServer(typeSelector.getSelectionModel().getSelectedItem());

        typeSelector.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            log.info("Selected server type: {}", newValue);
            userSettings.putServerType(Setting.SERVER_TYPE, newValue);
            server = createServer(newValue);
        });
        logSink.setLogConsumer(line -> Platform.runLater(() -> logArea.appendText(line + "\n")));
    }

    private void testRequirements() {
        requirementsStatusView.setStatus(RequirementsStatus.TESTING);
        if (server != null) {
            server.stop();
            boolean success = server.testRequirements();
            log.info("Can system run server? {}", success);
            requirementsStatusView.setStatus(success ? RequirementsStatus.SUCCESS : RequirementsStatus.FAILED);
        } else {
            log.error("No server selected");
            requirementsStatusView.setStatus(RequirementsStatus.FAILED);
        }
    }

    @FXML
    private void handleRun() {
        int port = Integer.parseInt(portField.getText());
        userSettings.putInt(Setting.PORT, port);
        userSettings.putString(Setting.SERVER_PATH, pathField.getText());
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

    private Server createServer(ServerType serverType) {
        server = serverFactory.getServer(serverType);
        server.statusProperty().addListener((_, _, newValue) -> {
            log.info("Server status changed: {}", newValue);
            Platform.runLater(() -> serverStatusView.setStatus(newValue));
        });
        Platform.runLater(() -> serverStatusView.setStatus(server.getStatus()));
        testRequirements();
        return server;
    }

}
