package org.schlunzis.kurtama.server.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatStoreTest {

    ChatStore chatStore;

    @BeforeEach
    void init() {
        chatStore = new ChatStore();
    }

    // ################################################
    // create()
    // ################################################

    @Test
    void createTest() {
        // mocked construction with power mockito -> not available in junit5

        Chat chat = chatStore.create();

        assertNotNull(chat);
        assertNotNull(chat.getId());
        assertTrue(chat.getChatters().isEmpty());
    }

    // ################################################
    // get(UUID)
    // ################################################

    @Test
    void getDefaultTest() {
        Chat createdChat = chatStore.create();

        Optional<Chat> chat = chatStore.get(createdChat.getId());

        assertTrue(chat.isPresent());
        assertEquals(createdChat, chat.get());
    }

    @Test
    void getOtherTest() {
        chatStore.create();
        chatStore.create();
        Chat createdChat = chatStore.create();
        chatStore.create();

        Optional<Chat> chat = chatStore.get(createdChat.getId());

        assertTrue(chat.isPresent());
        assertEquals(createdChat, chat.get());
    }

    @Test
    void getNullTest() {
        assertThrows(NullPointerException.class,
                () -> chatStore.get(null));
    }

    @Test
    void getNonExistentTest() {
        chatStore.create();

        Optional<Chat> chat = chatStore.get(UUID.randomUUID());

        assertTrue(chat.isEmpty());
    }

    // ################################################
    // remove(UUID)
    // ################################################

    @Test
    void removeDefaultTest() {
        Chat createdChat = chatStore.create();

        chatStore.remove(createdChat.getId());

        Optional<Chat> chat = chatStore.get(createdChat.getId());

        assertTrue(chat.isEmpty());
    }

    @Test
    void removeOtherTest() {
        chatStore.create();
        chatStore.create();
        Chat createdChat = chatStore.create();
        chatStore.create();

        chatStore.remove(createdChat.getId());

        Optional<Chat> chat = chatStore.get(createdChat.getId());

        assertTrue(chat.isEmpty());
    }

    @Test
    void removeNullTest() {
        assertThrows(NullPointerException.class,
                () -> chatStore.remove(null));
    }

    @Test
    void removeNonExistentTest() {
        chatStore.create();

        assertDoesNotThrow(() -> chatStore.remove(UUID.randomUUID()));
    }

}
