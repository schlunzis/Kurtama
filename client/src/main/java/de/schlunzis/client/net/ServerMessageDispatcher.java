package de.schlunzis.client.net;

import de.schlunzis.common.messages.IServerMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerMessageDispatcher {

    private final ApplicationEventPublisher eventBus;

    public void dispatch(IServerMessage message) {
        eventBus.publishEvent(message);
    }

}
