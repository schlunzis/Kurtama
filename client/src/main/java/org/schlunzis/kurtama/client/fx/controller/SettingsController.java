package org.schlunzis.kurtama.client.fx.controller;

import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.fx.scene.Scene;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.common.messages.authentication.delete.DeletionFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.delete.DeletionRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettingsController {

    private final ApplicationEventPublisher eventBus;

    @FXML
    private void initialize() {
        log.info("SettingsController initialized");
    }

    @FXML
    private void deleteAccount() {
        // TODO: require confirmation / password
        log.info("Delete account");
        eventBus.publishEvent(new DeletionRequest());
    }

    @FXML
    private void back() {
        eventBus.publishEvent(new SceneChangeEvent(Scene.MAIN));
    }

    @EventListener
    public void onDeletionFailedResponse(DeletionFailedResponse ignored) {
        log.info("Deletion failed");
    }
}
