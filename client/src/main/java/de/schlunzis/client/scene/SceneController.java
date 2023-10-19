package de.schlunzis.client.scene;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.client.scene.events.SceneChangeEvent;
import de.schlunzis.common.messages.authentication.LoginSuccessfulResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SceneController {

    private final EventBus eventBus;

    public SceneController(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void onLoginSuccessfulResponse(LoginSuccessfulResponse lsr) {
        eventBus.post(new SceneChangeEvent(Scene.MAIN));
    }

}
