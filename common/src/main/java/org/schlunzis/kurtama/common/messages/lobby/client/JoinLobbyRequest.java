package org.schlunzis.kurtama.common.messages.lobby.client;

import org.schlunzis.kurtama.common.messages.IClientMessage;

import java.util.UUID;

public record JoinLobbyRequest(UUID lobbyID) implements IClientMessage {
}
