package org.schlunzis.kurtama.client.fx.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.events.ConnectionStatusEvent;
import org.schlunzis.kurtama.client.events.NewServerConnectionEvent;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.client.settings.IUserSettings;
import org.schlunzis.kurtama.client.settings.Setting;
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

    public static final String NOT_CONNECTED_STYLE = "not_connected";
    public static final String CONNECTED_STYLE = "connected";
    public static final String CONNECTING_STYLE = "connecting";
    public static final String FAILED_STYLE = "failed";
    private static final int PROGRESS_ICON_SIZE = 30;

    private final ApplicationEventPublisher eventBus;
    private final Environment environment;
    private final ISessionService sessionService;
    private final IUserSettings userSettings;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;
    @FXML
    private Region progressIndicator;
    @FXML
    private Label progressLabel;

    private Rotate progressRotate;
    private Timeline progressTimeline;

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
        userSettings.putString(Setting.HOST, host);
        userSettings.putInt(Setting.PORT, port);
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

        progressRotate = new Rotate(0, PROGRESS_ICON_SIZE / 2d, PROGRESS_ICON_SIZE / 2d);
        progressTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressRotate.angleProperty(), 0d)),
                new KeyFrame(Duration.millis(2000), new KeyValue(progressRotate.angleProperty(), 360d))
        );
        progressIndicator.getTransforms().add(progressRotate);
        progressTimeline.setCycleCount(Animation.INDEFINITE);

        sessionService.getConnectionStatus().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> applyConnectionStatus(newValue))
        );
        applyConnectionStatus(sessionService.getConnectionStatus().getValue());
        serverField.setText(userSettings.getString(Setting.HOST));
        portField.setText(String.valueOf(userSettings.getInt(Setting.PORT)));
    }

    private void devLogin() {
        emailField.setText("test1@schlunzis.org");
        passwordField.setText("test1");
    }

    private void applyConnectionStatus(ConnectionStatusEvent.Status status) {
        String text = "";
        String indicatorClass = "";
        progressTimeline.stop();
        progressRotate.setAngle(0);
        switch (status) {
            case NOT_CONNECTED -> {
                text = "Not Connected";
                indicatorClass = NOT_CONNECTED_STYLE;
            }
            case CONNECTED -> {
                text = "Connected";
                indicatorClass = CONNECTED_STYLE;
            }
            case CONNECTING -> {
                text = "Connecting...";
                indicatorClass = CONNECTING_STYLE;
                progressTimeline.play();
            }
            case FAILED -> {
                text = "Connection Failed";
                indicatorClass = FAILED_STYLE;
            }
        }
        progressIndicator.getStyleClass().clear();
        progressIndicator.getStyleClass().addAll("progress-indicator", indicatorClass);
        progressLabel.setText(text);
    }

}
