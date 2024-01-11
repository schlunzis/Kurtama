package org.schlunzis.kurtama.server.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@RequiredArgsConstructor
public class ChatManagement {

    private final ChatStore chatStore;

    /**
     * maps from lobbyIDs to chatIDs
     */
    private final Map<UUID, UUID> lobbyChatMap = new HashMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public Chat createLobbyChat(UUID lobbyID) {
        Objects.requireNonNull(lobbyID);
        writeLock.lock();
        try {
            Chat chat = chatStore.create();
            lobbyChatMap.put(lobbyID, chat.getId());
            return chat;
        } finally {
            writeLock.unlock();
        }
    }

    public Optional<Chat> getLobbyChat(UUID lobbyID) {
        Objects.requireNonNull(lobbyID);
        readLock.lock();
        try {
            UUID chatID = lobbyChatMap.get(lobbyID);
            if (chatID == null)
                return Optional.empty();
            return chatStore.get(chatID);
        } finally {
            readLock.unlock();
        }
    }

    public void removeLobbyChat(UUID lobbyID) {
        Objects.requireNonNull(lobbyID);
        writeLock.lock();
        try {
            chatStore.remove(lobbyChatMap.get(lobbyID));
            lobbyChatMap.remove(lobbyID);
        } finally {
            writeLock.unlock();
        }
    }

    public Optional<Chat> getChat(UUID chatID) {
        readLock.lock();
        try {
            return chatStore.get(chatID);
        } finally {
            readLock.unlock();
        }
    }

}
