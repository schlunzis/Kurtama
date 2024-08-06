package org.schlunzis.kurtama.client.net;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.messages.IServerMessage;
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
