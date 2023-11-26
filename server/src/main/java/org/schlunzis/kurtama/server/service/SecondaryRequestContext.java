package org.schlunzis.kurtama.server.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.ClientMessageContext;
import org.schlunzis.kurtama.server.net.ResponseAssembler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SecondaryRequestContext<C extends IClientMessage, S extends IServerMessage> extends AbstractMessageContext implements ResolvableTypeProvider {

    private final ClientMessageContext<C> cmc;
    private final S mainResponse;

    public SecondaryRequestContext(S mainResponse, ClientMessageContext<C> cmc, ResponseAssembler responseAssembler,
                                   AuthenticationService authenticationService, ApplicationEventPublisher eventBus) {
        super(responseAssembler, authenticationService, eventBus, cmc.getSession(), cmc.getUser());
        this.cmc = cmc;
        this.mainResponse = mainResponse;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(),
                ResolvableType.forInstance(this.cmc.getClientMessage()),
                ResolvableType.forInstance(this.mainResponse)
        );
    }

}
