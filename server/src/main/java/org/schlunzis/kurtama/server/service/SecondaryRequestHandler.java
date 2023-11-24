package org.schlunzis.kurtama.server.service;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.chat.SecondaryChatService;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SecondaryRequestHandler {

    private final SecondaryChatService chatService;

    public Collection<IServerMessage> handle(IServerMessage message, ServerUser recipient) {
        Collection<IServerMessage> additionalMessages = new ArrayList<>();

        additionalMessages.addAll(chatService.onMessage(message, recipient));

        return additionalMessages;
    }

}
