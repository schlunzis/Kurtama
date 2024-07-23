package org.schlunzis.kurtama.common.messages.game.client;

import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.messages.IClientMessage;

import java.util.UUID;

public record StartGameRequest(UUID lobbyID,
                               GameSettings gameSettings) implements IClientMessage {

}
