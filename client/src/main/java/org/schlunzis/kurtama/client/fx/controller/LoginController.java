package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("login.fxml")
@Component
@RequiredArgsConstructor
public class LoginController {

    private final ApplicationEventPublisher eventBus;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

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

    @EventListener
    void onLoginFailedResponse(LoginFailedResponse lfr) {
        log.info("Received LoginFailedResponse {}", lfr);
        Platform.runLater(() -> passwordField.setText(""));
    }

}
