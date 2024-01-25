package org.schlunzis.kurtama.server.net.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(IServerMessage message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }

    public IClientMessage toClientMessage(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, IClientMessage.class);
    }

}
