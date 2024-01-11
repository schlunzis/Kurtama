package org.schlunzis.kurtama.server.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatTest {

    Chat chat;

    @Mock
    UUID chatID;

    @Mock
    ServerUser defaultUser;
    @Mock
    ServerUser secondUser;

    @BeforeEach
    void init() {
        chat = new Chat(chatID);
    }

    // ################################################
    // After Constructor
    // ################################################

    @Test
    void afterConstructorTest() {
        assertEquals(chatID, chat.getId());
        assertTrue(chat.getChatters().isEmpty());
    }

    // ################################################
    // addChatter(ServerUser)
    // ################################################

    @Test
    void addChatterNullTest() {
        assertThrows(NullPointerException.class,
                () -> chat.addChatter(null));
    }

    @Test
    void addChatterDefaultTest() {
        chat.addChatter(defaultUser);
        chat.addChatter(secondUser);

        Collection<ServerUser> users = chat.getChatters();
        assertEquals(2, users.size());
        assertTrue(users.contains(defaultUser));
        assertTrue(users.contains(secondUser));
    }

    @Test
    void addChatterSecondTimeTest() {
        chat.addChatter(defaultUser);
        chat.addChatter(defaultUser);

        Collection<ServerUser> users = chat.getChatters();
        assertEquals(1, users.size());
        assertTrue(users.contains(defaultUser));
    }

    // ################################################
    // removeChatter(ServerUser)
    // ################################################

    @Test
    void removeChatterNullTest() {
        assertThrows(NullPointerException.class,
                () -> chat.removeChatter(null));
    }

    @Test
    void removeChatterEmptyTest() {
        chat.removeChatter(defaultUser);

        Collection<ServerUser> users = chat.getChatters();

        assertTrue(users.isEmpty());
    }

    @Test
    void removeChatterDefaultTest() {
        chat.addChatter(defaultUser);
        chat.addChatter(secondUser);

        chat.removeChatter(defaultUser);

        Collection<ServerUser> users = chat.getChatters();
        assertEquals(1, users.size());
        assertTrue(users.contains(secondUser));
    }

    @Test
    void removeChatterSecondTimeTest() {
        chat.addChatter(defaultUser);
        chat.addChatter(secondUser);

        chat.removeChatter(defaultUser);
        chat.removeChatter(defaultUser);

        Collection<ServerUser> users = chat.getChatters();
        assertEquals(1, users.size());
        assertTrue(users.contains(secondUser));
    }

    // ################################################
    // getChatters()
    // ################################################

    @Test
    void getChattersDefaultTest() {
        Collection<ServerUser> users = chat.getChatters();
        assertTrue(users.isEmpty());

        chat.addChatter(defaultUser);

        users = chat.getChatters();
        assertEquals(1, users.size());
        assertTrue(users.contains(defaultUser));
    }

    @Test
    void getChattersEditTest() {
        chat.addChatter(defaultUser);

        Collection<ServerUser> users = chat.getChatters();
        users.remove(defaultUser);

        users = chat.getChatters();

        assertEquals(1, users.size());
        assertTrue(users.contains(defaultUser));
    }

}
