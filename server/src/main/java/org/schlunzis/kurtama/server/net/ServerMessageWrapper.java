package org.schlunzis.kurtama.server.net;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerMessageWrapper {

    private IServerMessage serverMessage;
    private Collection<ISession> recipients;

    public ServerMessageWrapper(IServerMessage message, ISession... sessions) {
        this(message, List.of(sessions));
    }

}
