package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.util.I18nUtils;
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

    @FXML
    private Label emailLabel;
    @FXML
    private TextField emailField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

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

    public void initialize() {
        // maybe relevant: https://stackoverflow.com/questions/25125563/clear-prompt-text-in-javafx-textfield-only-when-user-starts-typing
        emailLabel.textProperty().bind(I18nUtils.createBinding("login.email.label"));
        emailField.promptTextProperty().bind(I18nUtils.createBinding("login.email.prompt"));
        passwordLabel.textProperty().bind(I18nUtils.createBinding("login.password.label"));
        passwordField.promptTextProperty().bind(I18nUtils.createBinding("login.password.prompt"));

        loginButton.textProperty().bind(I18nUtils.createBinding("login.login.button"));
        registerButton.textProperty().bind(I18nUtils.createBinding("login.register.button"));


        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> (env.equalsIgnoreCase("dev"))))
            devLogin();
    }

    void devLogin() {
        emailField.setText("test1@schlunzis.org");
        passwordField.setText("test1");
    }

}
