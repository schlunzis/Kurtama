package de.schlunzis.client.controller;

import com.google.common.eventbus.EventBus;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainMenuController {

    private final EventBus eventBus;

    public MainMenuController(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @FXML
    private void logout() {
        log.info("Logout button clicked");
        // TODO implement, see issue
    }

}
