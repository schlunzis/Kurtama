package org.schlunzis.kurtama.client.service.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.service.IGameService;
import org.schlunzis.kurtama.common.game.model.IGameStateDTO;
import org.schlunzis.kurtama.common.messages.game.client.MoveRequest;
import org.schlunzis.kurtama.common.messages.game.server.GameStartedMessage;
import org.schlunzis.kurtama.common.messages.game.server.UpdateGameStateMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
class GameService implements IGameService {

    private final ApplicationEventPublisher eventBus;

    private final ObjectProperty<IGameStateDTO> gameState = new SimpleObjectProperty<>();
    private UUID gameID;

    @EventListener
    public void onGameStartedMessage(GameStartedMessage gsm) {
        gameState.set(gsm.gameState());
        gameID = gsm.gameID();
    }

    @EventListener
    public void onUpdateGameStateMessage(UpdateGameStateMessage gsm) {
        gameState.set(gsm.getGameState());
    }

    @Override
    public void sendMoveRequest(int id) {
        eventBus.publishEvent(new MoveRequest(gameID, id));
    }

}
