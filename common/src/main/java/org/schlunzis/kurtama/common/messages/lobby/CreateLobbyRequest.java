package org.schlunzis.kurtama.common.messages.lobby;

import org.schlunzis.kurtama.common.messages.IClientMessage;

public record CreateLobbyRequest(String name) implements IClientMessage {

}
