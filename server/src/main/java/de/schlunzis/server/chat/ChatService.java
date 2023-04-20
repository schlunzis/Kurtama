package de.schlunzis.server.chat;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.common.messages.chat.ClientChatMessage;
import de.schlunzis.common.messages.chat.ServerChatMessage;
import de.schlunzis.server.auth.AuthenticationService;
import de.schlunzis.server.net.MessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatService {

    private final EventBus eventBus;

    private final AuthenticationService authenticationService;

    public ChatService(EventBus eventBus, AuthenticationService authenticationService) {
        this.eventBus = eventBus;
        this.authenticationService = authenticationService;
        eventBus.register(this);
    }

    @Subscribe
    void onClientChatMessage(ClientChatMessage ccm) {
        log.debug("Processing chat message {}", ccm);
        eventBus.post(new MessageWrapper(new ServerChatMessage(ccm.getChatID(), ccm.getSender(), ccm.getMessage()), authenticationService.getAllLoggedInSessions()));
    }


}
