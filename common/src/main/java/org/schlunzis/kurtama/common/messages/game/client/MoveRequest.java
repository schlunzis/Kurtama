package org.schlunzis.kurtama.common.messages.game.client;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MoveRequest extends AbstractGameRequest {

    private final int fieldIndex;

    public MoveRequest(UUID gameID, int fieldIndex) {
        super(gameID);
        this.fieldIndex = fieldIndex;
    }

}
