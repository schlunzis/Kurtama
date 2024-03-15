package org.schlunzis.kurtama.common.messages.game.server;

import lombok.Getter;
import org.schlunzis.kurtama.common.game.model.IGameStateDTO;

import java.util.UUID;

@Getter
public class UpdateGameStateMessage extends AbstractGameMessage {

    private IGameStateDTO gameState;

    public UpdateGameStateMessage(UUID gameID, IGameStateDTO gameState) {
        super(gameID);
        this.gameState = gameState;
    }

    public UpdateGameStateMessage() {
        super(null);
    }

}
