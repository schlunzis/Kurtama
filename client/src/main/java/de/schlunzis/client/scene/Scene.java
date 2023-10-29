package de.schlunzis.client.scene;

import de.schlunzis.client.controller.LoginController;
import de.schlunzis.client.controller.MainMenuController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Scene {

    LOGIN("Login", LoginController.class),
    MAIN("Kurtama", MainMenuController.class);

    private final String titleKey;
    private final Class<?> controllerClass;

}
