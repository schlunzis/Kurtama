package de.schlunzis.server.net;

import de.schlunzis.common.messages.ClientMessage;

public record ClientMessageWrapper<T extends ClientMessage> (T clientMessage, Session session) {

}
