package org.schlunzis.kurtama.common.messages.game.client;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MoveRequest extends AbstractGameRequest {

    private int fieldIndex;

    public MoveRequest(UUID gameID, int fieldIndex) {
        super(gameID);
        this.fieldIndex = fieldIndex;
    }

    public MoveRequest() {
        super(null);
    }

}
