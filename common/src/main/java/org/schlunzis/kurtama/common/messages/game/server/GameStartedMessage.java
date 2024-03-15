package org.schlunzis.kurtama.common.messages.game.server;

import org.schlunzis.kurtama.common.game.model.IGameStateDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.UUID;

public record GameStartedMessage(UUID gameID, IGameStateDTO gameState) implements IServerMessage {

}
