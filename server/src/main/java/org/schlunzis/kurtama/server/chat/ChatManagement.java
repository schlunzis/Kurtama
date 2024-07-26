package org.schlunzis.kurtama.server.chat;

import lombok.Locked;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatManagement {

    private final ChatStore chatStore;

    /**
     * maps from lobbyIDs to chatIDs
     */
    private final Map<UUID, UUID> lobbyChatMap = new HashMap<>();

    @Locked.Write
    public Chat createLobbyChat(@NonNull UUID lobbyID) {
        Chat chat = chatStore.create();
        lobbyChatMap.put(lobbyID, chat.getId());
        return chat;
    }

    @Locked.Read
    public Optional<Chat> getLobbyChat(@NonNull UUID lobbyID) {
        UUID chatID = lobbyChatMap.get(lobbyID);
        if (chatID == null)
            return Optional.empty();
        return chatStore.get(chatID);
    }

    @Locked.Write
    public void removeLobbyChat(@NonNull UUID lobbyID) {
        chatStore.remove(lobbyChatMap.get(lobbyID));
        lobbyChatMap.remove(lobbyID);
    }

    @Locked.Read
    public Optional<Chat> getChat(@NonNull UUID chatID) {
        return chatStore.get(chatID);
    }

    @Locked.Write
    public void addChatter(@NonNull UUID chatID, @NonNull ServerUser user) {
        chatStore.get(chatID).ifPresentOrElse(
                chat -> chat.addChatter(user),
                () -> log.warn("Could not add user to chat. No chat with id {} found", chatID)
        );
    }

    @Locked.Write
    public void removeChatter(@NonNull UUID chatID, @NonNull ServerUser user) {
        chatStore.get(chatID).ifPresentOrElse(
                chat -> chat.removeChatter(user),
                () -> log.warn("Could not remove user from chat. No chat with id {} found", chatID)
        );
    }

}
