package de.schlunzis.server.chat;

import de.schlunzis.common.messages.chat.ClientChatMessage;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import de.schlunzis.server.auth.AuthenticationService;
import de.schlunzis.server.net.ClientMessageWrapper;
import de.schlunzis.server.net.ServerMessageWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatService {

    private final ApplicationEventPublisher eventBus;

    private final AuthenticationService authenticationService;


    @EventListener
    void onClientChatMessage(ClientMessageWrapper<ClientChatMessage> cmw) {
        ClientChatMessage ccm = cmw.clientMessage();
        log.debug("Processing chat message {}", ccm);
        eventBus.publishEvent(new ServerMessageWrapper(new ServerChatMessage(ccm.getChatID(), ccm.getSender(), ccm.getMessage()), authenticationService.getAllLoggedInSessions()));
    }


}
