package org.schlunzis.kurtama.server.chat;

import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.common.messages.chat.ClientChatMessage;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.ClientMessageWrapper;
import org.schlunzis.kurtama.server.service.AbstractService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ChatService extends AbstractService {

    private static final UUID GLOBAL_CHAT_ID = new UUID(0, 0);

    private final ChatManagement chatManagement;

    public ChatService(ApplicationEventPublisher eventBus, AuthenticationService authenticationService, ChatManagement chatManagement) {
        super(eventBus, authenticationService);
        this.chatManagement = chatManagement;
    }

    @EventListener
    public void onClientChatMessage(ClientMessageWrapper<ClientChatMessage> cmw) {
        ClientChatMessage ccm = cmw.clientMessage();
        log.debug("Processing chat message {}", ccm);

        if (ccm.getChatID() == null) {
            log.info("invalid chatID");
            return;
        }

        IServerMessage message = new ServerChatMessage(ccm.getChatID(), ccm.getNickname(), cmw.user().toDTO(), ccm.getMessage());
        if (ccm.getChatID().equals(GLOBAL_CHAT_ID)) {
            log.info("CHAT[GLOBAL]: {}; {}", ccm.getNickname(), ccm.getMessage());
            sendToAll(message);
        } else {
            chatManagement.getChat(ccm.getChatID()).ifPresentOrElse(
                    chat -> {
                        log.info("CHAT[{}]: {}; {}", ccm.getChatID(), ccm.getNickname(), ccm.getMessage());
                        sendToMany(message, chat.getChatters());
                    },
                    () -> log.info("No Chat for ChatMessage: {}", ccm)
            );
        }
    }

}
