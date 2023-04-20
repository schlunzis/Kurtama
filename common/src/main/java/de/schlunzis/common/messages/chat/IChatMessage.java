package de.schlunzis.common.messages.chat;

import java.util.UUID;

/**
 * All chat messages must implement this interface, independent of the direction.
 */
public interface IChatMessage {

    String getSender();

    String getMessage();

    UUID getChatID();

}
