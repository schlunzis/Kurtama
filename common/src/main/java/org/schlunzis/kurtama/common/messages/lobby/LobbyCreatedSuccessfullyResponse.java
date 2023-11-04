package org.schlunzis.kurtama.common.messages.lobby;

import org.schlunzis.kurtama.common.LobbyDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;

public record LobbyCreatedSuccessfullyResponse(LobbyDTO lobby) implements IServerMessage {
}
