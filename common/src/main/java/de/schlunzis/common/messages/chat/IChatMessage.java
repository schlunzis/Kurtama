package de.schlunzis.common.messages.chat;

import java.util.UUID;

/**
 * All chat messages must implement this interface, independent of the direction.
 */
public interface IChatMessage {

    String getNickname();

    String getMessage();

    UUID getChatID();

}
