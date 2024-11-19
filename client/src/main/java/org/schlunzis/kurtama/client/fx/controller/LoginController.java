package org.schlunzis.kurtama.client.fx.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.NotificationPane;
import org.schlunzis.kurtama.client.events.ConnectionStatusEvent;
import org.schlunzis.kurtama.client.events.NewServerConnectionEvent;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.NewStageEvent;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.fx.view.StatusView;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.client.settings.IUserSettings;
import org.schlunzis.kurtama.client.settings.Setting;
import org.schlunzis.kurtama.client.util.I18n;
import org.schlunzis.kurtama.client.util.VersionManager;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;


@Slf4j
@Component
public class LoginController extends AbstractController {

    public static final String NOT_CONNECTED_STYLE = "not_connected";
    public static final String CONNECTED_STYLE = "connected";
    public static final String CONNECTING_STYLE = "connecting";
    public static final String FAILED_STYLE = "failed";

    private final ApplicationEventPublisher eventBus;
    private final Environment environment;
    private final ISessionService sessionService;
    private final IUserSettings userSettings;
    private final VersionManager versionManager;

    public LoginController(I18n i18n, ApplicationEventPublisher eventBus, Environment environment, ISessionService sessionService, IUserSettings userSettings, VersionManager versionManager) {
        super(i18n);
        this.eventBus = eventBus;
        this.environment = environment;
        this.sessionService = sessionService;
        this.userSettings = userSettings;
        this.versionManager = versionManager;
    }

    @FXML
    @Getter(AccessLevel.PROTECTED)
    private NotificationPane notificationPane;

    // LOGIN FIELDS
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Locale> languageSelector;


    // SERVER CONNECTION FIELDS
    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;
    @FXML
    public StatusView statusView;
    @FXML
    private Label versionLabel;

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

        statusView.setI18n(i18n);
        sessionService.getConnectionStatus().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> applyConnectionStatus(newValue))
        );
        applyConnectionStatus(sessionService.getConnectionStatus().getValue());
        serverField.setText(userSettings.getString(Setting.HOST));
        portField.setText(String.valueOf(userSettings.getInt(Setting.PORT)));

        setVersion();

        languageSelector.setItems(FXCollections.observableList(i18n.getSUPPORTED_LOCALES()));
        languageSelector.setCellFactory(_ -> createLocaleCell());
        languageSelector.setButtonCell(createLocaleCell());
        languageSelector.setOnAction(_ -> i18n.setLocale(languageSelector.getSelectionModel().getSelectedItem()));
        languageSelector.getSelectionModel().select(i18n.getLocale());
        super.initNotificationPane();
    }

    private ListCell<Locale> createLocaleCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayName(item));
                }
            }
        };
    }

    private void devLogin() {
        emailField.setText("test1@schlunzis.org");
        passwordField.setText("test1");
    }

    private void applyConnectionStatus(ConnectionStatusEvent.Status status) {
        statusView.setStatus(status);
    }

    private void setVersion() {
        versionLabel.setText("v" + versionManager.getVersion());
    }

    public void createServer() {
        eventBus.publishEvent(new NewStageEvent(Scene.CREATE_SERVER));
    }

}
