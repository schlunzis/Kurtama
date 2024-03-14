package org.schlunzis.kurtama.common.messages.game.server;

import org.schlunzis.kurtama.common.game.model.IGameStateDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;

public record GameStartedMessage(IGameStateDTO gameState) implements IServerMessage {

}
