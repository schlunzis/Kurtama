package de.schlunzis.client.fx.controller;

import de.schlunzis.client.fx.scene.Scene;
import de.schlunzis.client.fx.scene.events.SceneChangeEvent;
import de.schlunzis.common.messages.authentication.register.RegisterFailedResponse;
import de.schlunzis.common.messages.authentication.register.RegisterRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
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
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        log.info("Register button clicked");
        String email = emailField.getText();
        String password = passwordField.getText();
        String username = "Jonas Doe";
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
        Platform.runLater(() -> passwordField.setText(""));
    }
}
