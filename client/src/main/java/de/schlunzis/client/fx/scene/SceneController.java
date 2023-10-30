package de.schlunzis.client.fx.scene;

import de.schlunzis.client.fx.scene.events.SceneChangeEvent;
import de.schlunzis.common.messages.authentication.login.LoginSuccessfulResponse;
import de.schlunzis.common.messages.authentication.logout.LogoutSuccessfulResponse;
import de.schlunzis.common.messages.authentication.register.RegisterSuccessfullResponse;
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

    @EventListener
    public void onLogoutSuccessfulResponse(LogoutSuccessfulResponse lsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOGIN));
    }

    @EventListener
    public void onRegisterSuccessfulResponse(RegisterSuccessfullResponse rsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOGIN));
    }

}
