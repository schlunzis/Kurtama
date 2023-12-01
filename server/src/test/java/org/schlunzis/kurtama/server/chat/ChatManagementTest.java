package org.schlunzis.kurtama.server.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatManagementTest {

    ChatManagement chatManagement;

    @Mock
    ChatStore chatStore;

    @Mock
    UUID chatID;

    @Mock
    UUID lobbyID;

    @Mock
    Chat defaultChat;

    @BeforeEach
    void init() {
        chatManagement = new ChatManagement(chatStore);
    }

    // ################################################
    // getLobbyChat(UUID)
    // ################################################

    @Test
    void createLobbyChatDefaultTest() {
        when(chatStore.create()).thenReturn(defaultChat);

        Chat chat = chatManagement.createLobbyChat(lobbyID);

        assertEquals(defaultChat, chat);
    }

    @Test
    void createLobbyChatNullTest() {
        assertThrows(NullPointerException.class,
                () -> chatManagement.createLobbyChat(null));
    }

    // ################################################
    // getLobbyChat(UUID)
    // ################################################

    @Test
    void getLobbyChatDefaultTest() {
        when(chatStore.create()).thenReturn(defaultChat);
        when(defaultChat.getId()).thenReturn(chatID);
        when(chatStore.get(chatID)).thenReturn(Optional.ofNullable(defaultChat));

        chatManagement.createLobbyChat(lobbyID);

        Optional<Chat> chat = chatManagement.getLobbyChat(lobbyID);

        assertTrue(chat.isPresent());
        assertEquals(defaultChat, chat.get());
    }

    @Test
    void getLobbyChatMultipleTest(@Mock Chat secondChat, @Mock UUID secondChatID, @Mock UUID secondLobbyID) {
        when(chatStore.get(chatID)).thenReturn(Optional.ofNullable(defaultChat));
        when(chatStore.get(secondChatID)).thenReturn(Optional.of(secondChat));
        when(defaultChat.getId()).thenReturn(chatID);
        when(secondChat.getId()).thenReturn(secondChatID);

        when(chatStore.create()).thenReturn(defaultChat);
        chatManagement.createLobbyChat(lobbyID);
        when(chatStore.create()).thenReturn(secondChat);
        chatManagement.createLobbyChat(secondLobbyID);

        Optional<Chat> chat = chatManagement.getLobbyChat(lobbyID);
        assertTrue(chat.isPresent());
        assertEquals(defaultChat, chat.get());
        chat = chatManagement.getLobbyChat(secondLobbyID);
        assertTrue(chat.isPresent());
        assertEquals(secondChat, chat.get());
    }

    @Test
    void getLobbyChatNullTest() {
        assertThrows(NullPointerException.class,
                () -> chatManagement.getLobbyChat(null));
    }

    // ################################################
    // removeLobbyChat(UUID)
    // ################################################

    @Test
    void removeLobbyChatDefaultTest() {
        when(chatStore.create()).thenReturn(defaultChat);
        when(defaultChat.getId()).thenReturn(chatID);

        chatManagement.createLobbyChat(lobbyID);

        chatManagement.removeLobbyChat(lobbyID);

        Optional<Chat> chat = chatManagement.getLobbyChat(lobbyID);

        assertTrue(chat.isEmpty());
    }

    @Test
    void removeLobbyChatMultipleTest(@Mock Chat secondChat, @Mock UUID secondChatID, @Mock UUID secondLobbyID) {
        when(chatStore.get(secondChatID)).thenReturn(Optional.of(secondChat));
        when(defaultChat.getId()).thenReturn(chatID);
        when(secondChat.getId()).thenReturn(secondChatID);

        when(chatStore.create()).thenReturn(defaultChat);
        chatManagement.createLobbyChat(lobbyID);
        when(chatStore.create()).thenReturn(secondChat);
        chatManagement.createLobbyChat(secondLobbyID);

        chatManagement.removeLobbyChat(lobbyID);

        Optional<Chat> chat = chatManagement.getLobbyChat(lobbyID);
        assertTrue(chat.isEmpty());
        chat = chatManagement.getLobbyChat(secondLobbyID);
        assertTrue(chat.isPresent());
        assertEquals(secondChat, chat.get());
    }

    @Test
    void removeLobbyChatNullTest() {
        assertThrows(NullPointerException.class,
                () -> chatManagement.removeLobbyChat(null));
    }

    // ################################################
    // getChat(UUID)
    // ################################################

    @Test
    void getChatDefaultTest() {
        when(chatStore.get(chatID)).thenReturn(Optional.ofNullable(defaultChat));

        Optional<Chat> chat = chatManagement.getChat(chatID);

        assertTrue(chat.isPresent());
        assertEquals(defaultChat, chat.get());
    }

    @Test
    void getChatNullTest() {
        when(chatStore.get(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class,
                () -> chatManagement.getChat(null));
    }

}
