package org.schlunzis.kurtama.server.net.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageConverterTest {

    MessageConverter messageConverter;

    @BeforeEach
    void init() {
        messageConverter = new MessageConverter();
    }

    @Test
    void toJsonTest() throws JsonProcessingException {
        IServerMessage message = new TestMessage();
        assertEquals(TestMessage.JSON, messageConverter.toJson(message));
    }

    @Test
    void toClientMessageTest() throws JsonProcessingException {
        // FIXME either remove or make an inheritance type id resolver like in common
        //assertEquals(new TestMessage(), messageConverter.toClientMessage(TestMessage.JSON));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestMessage implements IClientMessage, IServerMessage {

        public static final String JSON = "{\"discriminator\":\"TestMessage\",\"s\":\"test\"}";

        private String s = "test";

    }
}
