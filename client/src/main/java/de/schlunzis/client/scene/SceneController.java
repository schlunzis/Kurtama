package de.schlunzis.client.scene;

import de.schlunzis.client.scene.events.SceneChangeEvent;
import de.schlunzis.common.messages.authentication.LoginSuccessfulResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SceneController {

    private final ApplicationEventPublisher eventBus;

    @EventListener
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse lsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.MAIN));
    }

}
