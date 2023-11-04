package org.schlunzis.kurtama.common.messages.lobby.server;

import org.schlunzis.kurtama.common.LobbyDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;

// TODO dont send users, only user count
public record LobbyListInfoMessage(Collection<LobbyDTO> lobbies) implements IServerMessage {
}
