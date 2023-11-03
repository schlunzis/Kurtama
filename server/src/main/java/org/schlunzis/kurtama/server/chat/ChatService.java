package org.schlunzis.kurtama.server.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.messages.chat.ClientChatMessage;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.auth.UserSessionMap;
import org.schlunzis.kurtama.server.net.ClientMessageWrapper;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatService {

    private final ApplicationEventPublisher eventBus;

    private final AuthenticationService authenticationService;

    private final UserSessionMap userSessionMap;


    @EventListener
    public void onClientChatMessage(ClientMessageWrapper<ClientChatMessage> cmw) {
        ClientChatMessage ccm = cmw.clientMessage();
        log.debug("Processing chat message {}", ccm);
        Optional<ServerUser> optionalUser = userSessionMap.get(cmw.session());
        if (optionalUser.isPresent())
            eventBus.publishEvent(new ServerMessageWrapper(
                    new ServerChatMessage(ccm.getChatID(), ccm.getNickname(), optionalUser.get().toDTO(), ccm.getMessage()),
                    authenticationService.getAllLoggedInSessions()));
        else
            log.warn("received ClientChatMessage without user for session!");
    }


}
