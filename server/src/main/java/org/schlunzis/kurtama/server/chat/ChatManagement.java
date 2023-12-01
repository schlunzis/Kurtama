package org.schlunzis.kurtama.server.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ChatManagement {

    private final ChatStore chatStore;

    /**
     * maps from lobbyIDs to chatIDs
     */
    private final Map<UUID, UUID> lobbyChatMap = new HashMap<>();

    public Chat createLobbyChat(UUID lobbyID) {
        Objects.requireNonNull(lobbyID);
        Chat chat = chatStore.create();
        lobbyChatMap.put(lobbyID, chat.getId());
        return chat;
    }

    public Optional<Chat> getLobbyChat(UUID lobbyID) {
        Objects.requireNonNull(lobbyID);
        UUID chatID = lobbyChatMap.get(lobbyID);
        if (chatID == null)
            return Optional.empty();
        return chatStore.get(chatID);
    }

    public void removeLobbyChat(UUID lobbyID) {
        Objects.requireNonNull(lobbyID);
        chatStore.remove(lobbyChatMap.get(lobbyID));
        lobbyChatMap.remove(lobbyID);
    }

    public Optional<Chat> getChat(UUID chatID) {
        return chatStore.get(chatID);
    }

}
