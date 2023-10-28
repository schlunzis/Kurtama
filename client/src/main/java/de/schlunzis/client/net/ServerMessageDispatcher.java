package de.schlunzis.client.net;

import de.schlunzis.common.messages.ServerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerMessageDispatcher {

    private final ApplicationEventPublisher eventBus;

    public void dispatch(ServerMessage message) {
        eventBus.publishEvent(message);
    }

}
