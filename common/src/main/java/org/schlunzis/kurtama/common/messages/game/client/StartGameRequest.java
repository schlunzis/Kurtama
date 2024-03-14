package org.schlunzis.kurtama.common.messages.game.client;

import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.messages.IClientMessage;

public record StartGameRequest(GameSettings gameSettings) implements IClientMessage {

}
