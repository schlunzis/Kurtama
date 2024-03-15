package org.schlunzis.kurtama.common.messages.game.server;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractGameMessage implements IServerMessage {

    private final UUID gameID;

}
