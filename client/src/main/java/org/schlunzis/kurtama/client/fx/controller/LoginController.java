package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.events.ConnectionStatusEvent;
import org.schlunzis.kurtama.client.events.NewServerConnectionEvent;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.net.NetworkSettings;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@FxmlView("login.fxml")
@Component
@RequiredArgsConstructor
public class LoginController {

    private final ApplicationEventPublisher eventBus;
    private final Environment environment;
    private final ISessionService sessionService;
    private final NetworkSettings networkSettings;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label progressLabel;

    @FXML
    private void handleLogin() {
        log.info("Login button clicked");
        String email = emailField.getText();
        String password = passwordField.getText();
        LoginRequest lr = new LoginRequest(email, password);
        eventBus.publishEvent(lr);
    }

    @FXML
    private void handleRegister() {
        log.info("Register button clicked");
        eventBus.publishEvent(new SceneChangeEvent(Scene.REGISTER));
    }

    @FXML
    private void handleServerConnect() {
        String host = serverField.getText();
        int port = Integer.parseInt(portField.getText());
        // TODO handle invalid inputs
        eventBus.publishEvent(new NewServerConnectionEvent(host, port));
    }

    @EventListener
    void onLoginFailedResponse(LoginFailedResponse lfr) {
        log.info("Received LoginFailedResponse {}", lfr);
        Platform.runLater(() -> passwordField.setText(""));
    }

    @FXML
    private void initialize() {
        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> (env.equalsIgnoreCase("dev"))))
            devLogin();
        sessionService.getConnectionStatus().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> applyConnectionStatus(newValue))
        );
        applyConnectionStatus(sessionService.getConnectionStatus().getValue());
        serverField.setText(networkSettings.getHost());
        portField.setText(String.valueOf(networkSettings.getPort()));
    }

    private void devLogin() {
        emailField.setText("test1@schlunzis.org");
        passwordField.setText("test1");
    }

    private void applyConnectionStatus(ConnectionStatusEvent.Status status) {
        double progressValue = 0;
        String text = "";
        switch (status) {
            case NOT_CONNECTED -> {
                progressValue = 0;
                text = "Not Connected";
            }
            case CONNECTED -> {
                progressValue = 1;
                text = "Connected";
            }
            case CONNECTING -> {
                progressValue = -1;
                text = "Connecting...";
            }
            case FAILED -> {
                progressValue = -2;
                text = "Connection Failed";
            }
        }
        progressIndicator.setProgress(progressValue);
        progressLabel.setText(text);
    }

}
