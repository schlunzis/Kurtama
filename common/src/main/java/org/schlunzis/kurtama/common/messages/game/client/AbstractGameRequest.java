package org.schlunzis.kurtama.common.messages.game.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.messages.IClientMessage;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class AbstractGameRequest implements IClientMessage {

    private final UUID gameID;

}
