package org.schlunzis.kurtama.common.messages.chat;

import org.schlunzis.kurtama.common.messages.IServerMessage;

/**
 * This message is sent back by the server, if a chat message could not be processed correctly
 */
public record ChatMessageExceptionMessage(ErrorCode errorCode) implements IServerMessage {

    public enum ErrorCode {
        INVALID_CHAT_ID,
        INVALID_NICKNAME,
        INVALID_MESSAGE
    }

}
