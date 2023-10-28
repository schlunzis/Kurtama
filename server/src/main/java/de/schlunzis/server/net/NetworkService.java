package de.schlunzis.server.net;

import de.schlunzis.common.messages.ServerMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NetworkService {

    private final NetworkServer networkServer;


    public void start() {
        networkServer.start();
    }

    @EventListener
    public void onServerMessage(ServerMessage serverMessage) {
        networkServer.sendMessage(serverMessage);
    }

    @EventListener
    public void onMessageWrapper(ServerMessageWrapper messageWrapper) {
        log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
        networkServer.sendMessage(messageWrapper.getServerMessage(), messageWrapper.getRecipients());
    }


}
