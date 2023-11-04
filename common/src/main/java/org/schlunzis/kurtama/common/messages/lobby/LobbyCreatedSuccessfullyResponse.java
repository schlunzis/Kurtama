package org.schlunzis.kurtama.common.messages.lobby;

import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.messages.IServerMessage;

public record LobbyCreatedSuccessfullyResponse(ILobby lobby) implements IServerMessage {
}
