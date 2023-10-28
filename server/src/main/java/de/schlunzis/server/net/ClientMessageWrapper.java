package de.schlunzis.server.net;

import de.schlunzis.common.messages.ClientMessage;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * thanks to <a href="https://stackoverflow.com/questions/71452445/eventlistener-for-generic-events-with-spring">StackOverflow</a>
 */
public record ClientMessageWrapper<T extends ClientMessage>(T clientMessage,
                                                            Session session) implements ResolvableTypeProvider {

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forInstance(this.clientMessage)
        );
    }
}
