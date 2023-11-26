package org.schlunzis.kurtama.server.service;

import lombok.Getter;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.ClientMessageContext;
import org.schlunzis.kurtama.server.net.ResponseAssembler;
import org.springframework.context.ApplicationEventPublisher;

@Getter
public class SecondaryRequestContext<C extends IClientMessage, S extends IServerMessage> extends ClientMessageContext<IClientMessage> {

    private final S mainResponse;

    public SecondaryRequestContext(S mainResponse, ClientMessageContext<C> cmc, ResponseAssembler responseAssembler,
                                   AuthenticationService authenticationService, ApplicationEventPublisher eventBus) {
        super(cmc.getClientMessage(), cmc.getSession(), cmc.getUser(), responseAssembler, authenticationService, eventBus);
        this.mainResponse = mainResponse;
    }

}
