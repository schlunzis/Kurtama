package org.schlunzis.kurtama.server.net;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerMessageWrapper {

    private IServerMessage serverMessage;
    private Collection<ISession> recipients;
    private Collection<IServerMessage> additionalMessages;

    public ServerMessageWrapper(IServerMessage message, ISession... sessions) {
        this(message, List.of(sessions), Collections.emptyList());
    }

    public ServerMessageWrapper(IServerMessage message, Collection<ISession> sessions) {
        this(message, sessions, Collections.emptyList());
    }

    public ServerMessageWrapper(IServerMessage message, ISession session, Collection<IServerMessage> additionalMessages) {
        this(message, List.of(session), additionalMessages);
    }

}
