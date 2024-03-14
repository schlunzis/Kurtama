package org.schlunzis.kurtama.client.fx.scene;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.game.server.GameStartedMessage;
import org.schlunzis.kurtama.common.messages.lobby.server.JoinLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LeaveLobbySuccessfullyResponse;
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
    public void onRegisterSuccessfulResponse(RegisterSuccessfulResponse rsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOGIN));
    }

    @EventListener
    public void onLobbyCreatedSuccessfullyResponse(LobbyCreatedSuccessfullyResponse lcsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOBBY));
    }

    @EventListener
    public void onJoinLobbySuccessfullyResponse(JoinLobbySuccessfullyResponse jlsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.LOBBY));
    }

    @EventListener
    public void onLeaveLobbySuccessfullyResponse(LeaveLobbySuccessfullyResponse llsr) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.MAIN));
    }

    @EventListener
    public void onGameStartedMessage(GameStartedMessage gsm) {
        eventBus.publishEvent(new SceneChangeEvent(Scene.GAME));
    }

}
