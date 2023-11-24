package org.schlunzis.kurtama.server.chat;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.common.messages.lobby.server.JoinLobbySuccessfullyResponse;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SecondaryChatService {


    public Collection<IServerMessage> onMessage(IServerMessage message, ServerUser recipient) {
        Collection<IServerMessage> additionalMessages = new ArrayList<>();
        if (message instanceof JoinLobbySuccessfullyResponse lobbySuccessfullyResponse) {

        }
        return additionalMessages;
    }

}
