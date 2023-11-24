package org.schlunzis.kurtama.server.chat;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
class ChatStore {

    private final Map<UUID, Chat> chats = new HashMap<>();

    public Chat create() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (chats.containsKey(uuid));

        Chat chat = new Chat(uuid);
        chats.put(uuid, chat);
        return chat;
    }

    public void remove(UUID uuid) {
        chats.remove(uuid);
    }

    public Optional<Chat> get(UUID uuid) {
        return Optional.ofNullable(chats.get(uuid));
    }

}
