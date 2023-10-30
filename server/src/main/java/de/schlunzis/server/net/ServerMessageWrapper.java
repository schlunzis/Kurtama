package de.schlunzis.server.net;

import de.schlunzis.common.messages.IServerMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServerMessageWrapper {

    private IServerMessage serverMessage;
    private Collection<ISession> recipients;

    public ServerMessageWrapper(IServerMessage message, ISession... sessions) {
        this(message, List.of(sessions));
    }

}
