package de.schlunzis.client.net;

import com.google.common.eventbus.EventBus;
import de.schlunzis.common.messages.ServerMessage;
import org.springframework.stereotype.Component;

@Component
public class ServerMessageDispatcher {

    private final EventBus eventBus;

    public ServerMessageDispatcher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void dispatch(ServerMessage message) {
        eventBus.post(message);
    }

}
