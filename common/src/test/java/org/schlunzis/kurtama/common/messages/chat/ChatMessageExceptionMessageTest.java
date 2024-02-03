package org.schlunzis.kurtama.common.messages.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessageExceptionMessageTest {

    @Test
    void testCreateWithValidErrorCode() {
        ChatMessageExceptionMessage message = new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_NICKNAME);

        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_NICKNAME, message.errorCode());
    }

    @Test
    void testCreateWithInvalidErrorCode() {
        try {
            new ChatMessageExceptionMessage(null);
            fail("Creating a message with a null errorCode should throw an exception");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    void testEquals() {
        ChatMessageExceptionMessage message1 = new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_CHAT_ID);
        ChatMessageExceptionMessage message2 = new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_NICKNAME);
        ChatMessageExceptionMessage message3 = new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_CHAT_ID);

        assertNotEquals(message1, message2);
        assertEquals(message1, message3);
    }
}