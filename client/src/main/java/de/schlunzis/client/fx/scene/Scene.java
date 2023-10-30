package de.schlunzis.client.fx.scene;

import de.schlunzis.client.fx.controller.LoginController;
import de.schlunzis.client.fx.controller.MainMenuController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum lists all scenes of the application. Each entry stores information about the title key and the controller class of the scene.
 *
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Scene {

    LOGIN("Login", LoginController.class),
    MAIN("Kurtama", MainMenuController.class);

    /**
     * Contains the lookup key for the title of the scene.
     */
    private final String titleKey;
    /**
     * Contains the controller class for the scene.
     */
    private final Class<?> controllerClass;

}
