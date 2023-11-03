package org.schlunzis.kurtama.server.net;

import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * thanks to <a href="https://stackoverflow.com/questions/71452445/eventlistener-for-generic-events-with-spring">StackOverflow</a>
 */
public record ClientMessageWrapper<T extends IClientMessage>(T clientMessage,
                                                             ISession session) implements ResolvableTypeProvider {

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forInstance(this.clientMessage)
        );
    }
}
