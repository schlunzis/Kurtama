package org.schlunzis.kurtama.common.messages;

import java.util.List;

public record ServerMessageBundle(List<IServerMessage> messages) implements IServerMessage {

}
