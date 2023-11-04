package org.schlunzis.kurtama.client.fx.scene;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterSuccessfullResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LobbyCreatedSuccessfullyResponse;
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

    @EventListener
    public void onLobbyCreatedSuccessfullyResponse(LobbyCreatedSuccessfullyResponse lcsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOBBY));
    }

}
