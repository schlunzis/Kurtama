package org.schlunzis.kurtama.server.chat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
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

    public void addChatter(@NonNull UUID chatID, @NonNull ServerUser user) {
        writeLock.lock();
        try {
            chatStore.get(chatID).ifPresentOrElse(
                    chat -> chat.addChatter(user),
                    () -> log.warn("Could not add user to chat. No chat with id {} found", chatID)
            );
        } finally {
            writeLock.unlock();
        }
    }

    public void removeChatter(@NonNull UUID chatID, @NonNull ServerUser user) {
        writeLock.lock();
        try {
            chatStore.get(chatID).ifPresentOrElse(
                    chat -> chat.removeChatter(user),
                    () -> log.warn("Could not remove user from chat. No chat with id {} found", chatID)
            );
        } finally {
            writeLock.unlock();
        }
    }

}
