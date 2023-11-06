package org.schlunzis.kurtama.common.messages.lobby.server;

import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;

public record LobbyListInfoMessage(Collection<LobbyInfo> lobbies) implements IServerMessage {
}
