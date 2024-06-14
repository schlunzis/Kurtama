package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettingsController {

    private final ApplicationEventPublisher eventBus;
    private final ISessionService sessionService;

    @FXML
    private void initialize() {
        log.info("SettingsController initialized");
    }

    @FXML
    private void deleteAccount() {
        log.info("Delete account");
    }

    @FXML
    private void back() {
        eventBus.publishEvent(new SceneChangeEvent(Scene.MAIN));
    }
}
