package org.schlunzis.kurtama.server.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatManagement {

    private final ChatStore chatStore;
    private final Map<UUID, UUID> lobbyChatMap = new HashMap<>();

    public Chat createLobbyChat(UUID lobbyID) {
        Chat chat = chatStore.create();
        lobbyChatMap.put(lobbyID, chat.getId());
        return chat;
    }

    public Optional<Chat> getLobbyChat(UUID lobbyID) {
        return chatStore.get(lobbyChatMap.get(lobbyID));
    }

    public void removeLobbyChat(UUID lobbyID) {
        chatStore.remove(lobbyChatMap.get(lobbyID));
    }

}
