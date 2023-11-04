package org.schlunzis.kurtama.common.messages.lobby.server;

import org.schlunzis.kurtama.common.LobbyDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;

public record JoinLobbySuccessfullyResponse(LobbyDTO lobby) implements IServerMessage {
}
