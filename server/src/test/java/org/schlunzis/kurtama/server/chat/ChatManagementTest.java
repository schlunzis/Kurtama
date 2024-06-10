package org.schlunzis.kurtama.server.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // ################################################
    // addChatter(UUID, ServerUser)
    // ################################################

    @Test
    void addChatterDefaultTest(@Mock ServerUser user) {
        when(chatStore.get(chatID)).thenReturn(Optional.ofNullable(defaultChat));
        chatManagement.addChatter(chatID, user);
        verify(defaultChat).addChatter(user);
    }

    @Test
    void addChatterNonExistingChatTest(@Mock ServerUser user) {
        when(chatStore.get(chatID)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> chatManagement.addChatter(chatID, user));
        verify(defaultChat, never()).addChatter(user);
    }

    @Test
    void addChatterNullTest(@Mock ServerUser user) {
        assertThrows(NullPointerException.class, () -> chatManagement.addChatter(null, user));
        assertThrows(NullPointerException.class, () -> chatManagement.addChatter(chatID, null));
    }

    // ################################################
    // removeChatter(UUID, ServerUser)
    // ################################################

    @Test
    void removeChatterDefaultTest(@Mock ServerUser user) {
        when(chatStore.get(chatID)).thenReturn(Optional.ofNullable(defaultChat));
        chatManagement.removeChatter(chatID, user);
        verify(defaultChat).removeChatter(user);
    }

    @Test
    void removeChatterNonExistingChatTest(@Mock ServerUser user) {
        when(chatStore.get(chatID)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> chatManagement.removeChatter(chatID, user));
        verify(defaultChat, never()).removeChatter(user);
    }

    @Test
    void removeChatterNullTest(@Mock ServerUser user) {
        assertThrows(NullPointerException.class, () -> chatManagement.removeChatter(null, user));
        assertThrows(NullPointerException.class, () -> chatManagement.removeChatter(chatID, null));
    }

}
