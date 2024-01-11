package org.schlunzis.kurtama.server.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.common.messages.chat.ChatMessageExceptionMessage;
import org.schlunzis.kurtama.common.messages.chat.ClientChatMessage;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatService {

    private static final UUID GLOBAL_CHAT_ID = new UUID(0, 0);

    private final ChatManagement chatManagement;

    @Async
    @EventListener
    public void onClientChatMessage(final ClientMessageContext<ClientChatMessage> cmc) {
        final ClientChatMessage ccm = cmc.getClientMessage();
        log.debug("Processing chat message {}", ccm);

        if (!validateArguments(cmc)) {
            cmc.close();
            return;
        }

        final IServerMessage message = new ServerChatMessage(ccm.getChatID(), ccm.getNickname(), cmc.getUser().toDTO(), ccm.getMessage());
        if (ccm.getChatID().equals(GLOBAL_CHAT_ID)) {
            log.info("CHAT[GLOBAL]: {}; {}", ccm.getNickname(), ccm.getMessage());
            cmc.sendToAll(message);
        } else {
            chatManagement.getChat(ccm.getChatID()).ifPresentOrElse(
                    chat -> {
                        log.info("CHAT[{}]: {}; {}", ccm.getChatID(), ccm.getNickname(), ccm.getMessage());
                        cmc.sendToMany(message, chat.getChatters());
                    },
                    () -> {
                        log.info("No Chat for ChatMessage: {}", ccm);
                        cmc.respond(new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_CHAT_ID));
                    }
            );
        }
        cmc.close();
    }

    private boolean validateArguments(ClientMessageContext<ClientChatMessage> cmc) {
        final ClientChatMessage ccm = cmc.getClientMessage();

        if (ccm.getChatID() == null) {
            log.info("chat id is null");
            cmc.respond(new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_CHAT_ID));
            return false;
        }
        if (ccm.getNickname() == null || ccm.getNickname().isBlank()) {
            log.info("nickname id is invalid");
            cmc.respond(new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_NICKNAME));
            return false;
        }
        if (ccm.getMessage() == null || ccm.getMessage().isBlank()) {
            log.info("message id is invalid");
            cmc.respond(new ChatMessageExceptionMessage(ChatMessageExceptionMessage.ErrorCode.INVALID_MESSAGE));
            return false;
        }

        return true;
    }

}
