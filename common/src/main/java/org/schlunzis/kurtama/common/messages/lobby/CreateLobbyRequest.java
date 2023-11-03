package org.schlunzis.kurtama.common.messages.lobby;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.messages.IClientMessage;

@RequiredArgsConstructor
public class CreateLobbyRequest implements IClientMessage {

    private final String name;

}
