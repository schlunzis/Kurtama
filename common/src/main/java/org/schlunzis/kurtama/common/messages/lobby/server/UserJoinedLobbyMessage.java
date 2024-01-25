package org.schlunzis.kurtama.common.messages.lobby.server;

import org.schlunzis.kurtama.common.UserDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;

public record UserJoinedLobbyMessage(UserDTO joiningLobbyMember) implements IServerMessage {
}
