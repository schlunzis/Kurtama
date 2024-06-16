package org.schlunzis.kurtama.client.fx.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.fx.controller.*;

/**
 * This enum lists all scenes of the application. Each entry stores information about the title key and the controller class of the scene.
 *
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Scene {

    LOGIN("login", LoginController.class, "login.fxml"),
    MAIN("main", MainMenuController.class, "main.fxml"),
    REGISTER("register", RegisterController.class, "register.fxml"),
    LOBBY("lobby", LobbyController.class, "lobby.fxml"),
    SETTINGS("settings", SettingsController.class, "settings.fxml");

    /**
     * Contains the lookup key for the title of the scene.
     */
    private final String titleKey;
    /**
     * Contains the controller class for the scene.
     */
    private final Class<?> controllerClass;

    private final String fxml;

}
