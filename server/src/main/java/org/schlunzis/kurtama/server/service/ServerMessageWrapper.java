package org.schlunzis.kurtama.server.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ServerMessageWrapper {

    private IServerMessage serverMessage;
    private Collection<ServerUser> recipients;

    public ServerMessageWrapper(IServerMessage message, ServerUser... serverUsers) {
        this(message, List.of(serverUsers));
    }

}
