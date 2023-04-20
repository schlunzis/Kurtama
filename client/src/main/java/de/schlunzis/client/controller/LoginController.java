package de.schlunzis.client.controller;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.common.messages.authentication.LoginFailedResponse;
import de.schlunzis.common.messages.authentication.LoginRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginController {

    private final EventBus eventBus;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    public LoginController(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }


    @FXML
    private void handleLogin() {
        log.info("Login button clicked");
        String email = emailField.getText();
        String password = passwordField.getText();
        LoginRequest lr = new LoginRequest(email, password);
        eventBus.post(lr);
    }

    @Subscribe
    void onLoginFailedResponse(LoginFailedResponse lfr) {
        log.info("Received LoginFailedResponse {}", lfr);
        Platform.runLater(() -> passwordField.setText(""));
    }
    
}
