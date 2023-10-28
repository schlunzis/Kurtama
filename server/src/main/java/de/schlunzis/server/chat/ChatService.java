package de.schlunzis.server.chat;

import de.schlunzis.common.messages.chat.ClientChatMessage;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import de.schlunzis.server.auth.AuthenticationService;
import de.schlunzis.server.auth.UserSessionMap;
import de.schlunzis.server.net.ClientMessageWrapper;
import de.schlunzis.server.net.ServerMessageWrapper;
import de.schlunzis.server.user.ServerUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
