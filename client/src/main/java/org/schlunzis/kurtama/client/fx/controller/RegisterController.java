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
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@FxmlView("register.fxml")
@Component
@RequiredArgsConstructor
public class RegisterController {

    private final ApplicationEventPublisher eventBus;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordRepeatField;

    // @FXML
    // private JFXSnackbar snackbar;

    @FXML
    private void handleRegister() {
        log.info("Register button clicked");
        String password = passwordField.getText();
        String passwordRepeat = passwordRepeatField.getText();
        if (password.isBlank() || !password.equals(passwordRepeat)) {
            log.info("Passwords do not match or password is empty");
            Platform.runLater(() -> {
                passwordField.setText("");
                passwordRepeatField.setText("");
            });
            return;
        }
        String email = emailField.getText();
        String username = usernameField.getText();
        RegisterRequest rr = new RegisterRequest(username, email, password);
        eventBus.publishEvent(rr);
    }

    @FXML
    private void handleBack() {
        log.info("Back button clicked");
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOGIN));
    }

    @EventListener
    void onRegisterFailedResponse(RegisterFailedResponse rfr) {
        log.info("Received RegisterFailedResponse {}", rfr);
        Platform.runLater(() -> {
            passwordField.setText("");
            passwordRepeatField.setText("");
            // snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Registration Failed"), Duration.seconds(2), null));
        });
    }
}
