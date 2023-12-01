package org.schlunzis.kurtama.client.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.service.ILobbyService;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.messages.lobby.client.LeaveLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.server.JoinLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LeaveLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LobbyCreatedSuccessfullyResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class LobbyService implements ILobbyService {

    private final ApplicationEventPublisher eventBus;

    private Optional<ILobby> currentLobby = Optional.empty();

    @EventListener
    public void onLobbyCreatedSuccessfullyResponse(LobbyCreatedSuccessfullyResponse csr) {
        currentLobby = Optional.of(csr.lobby());
    }

    @EventListener
    public void onJoinLobbySuccessfullyResponse(JoinLobbySuccessfullyResponse jsr) {
        currentLobby = Optional.of(jsr.lobby());
    }

    @EventListener
    public void onLeaveLobbySuccessfullyResponse(LeaveLobbySuccessfullyResponse ignored) {
        currentLobby = Optional.empty();
    }

    public void leaveLobby() {
        currentLobby.ifPresent(lobby ->
                eventBus.publishEvent(new LeaveLobbyRequest(lobby.getId()))
        );
    }

}
