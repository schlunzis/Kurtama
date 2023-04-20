package de.schlunzis.server.net;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.common.messages.ServerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NetworkService {

    private final NetworkServer networkServer;


    public NetworkService(NetworkServer networkServer, EventBus eventBus) {
        this.networkServer = networkServer;
        eventBus.register(this);
    }

    public void start() {
        networkServer.start();
    }

    @Subscribe
    public void onServerMessage(ServerMessage serverMessage) {
        networkServer.sendMessage(serverMessage);
    }

    @Subscribe
    public void onMessageWrapper(MessageWrapper messageWrapper) {
        log.debug("Sending messages to {} recipients", messageWrapper.getRecipients().size());
        networkServer.sendMessage(messageWrapper.getServerMessage(), messageWrapper.getRecipients());
    }


}
