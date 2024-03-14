package org.schlunzis.kurtama.client.service.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import org.schlunzis.kurtama.client.service.IGameService;
import org.schlunzis.kurtama.common.game.model.IGameStateDTO;
import org.schlunzis.kurtama.common.messages.game.server.GameStartedMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
class GameService implements IGameService {

    @Getter
    private final ObjectProperty<IGameStateDTO> gameState = new SimpleObjectProperty<>();

    @EventListener
    public void onGameStartedMessage(GameStartedMessage gsm) {
        gameState.set(gsm.gameState());
    }

}
