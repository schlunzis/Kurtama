package de.schlunzis.server.net;

import de.schlunzis.common.messages.ServerMessage;
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
public class MessageWrapper {

    private ServerMessage serverMessage;
    private Collection<Session> recipients;

    public MessageWrapper(ServerMessage message, Session... sessions) {
        this(message, List.of(sessions));
    }

}
