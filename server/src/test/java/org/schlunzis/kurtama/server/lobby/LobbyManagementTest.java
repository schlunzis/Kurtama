package org.schlunzis.kurtama.server.lobby;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.server.chat.Chat;
import org.schlunzis.kurtama.server.chat.ChatManagement;
import org.schlunzis.kurtama.server.lobby.exception.LobbyNotFoundException;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LobbyManagementTest {

    LobbyManagement lobbyManagement;

    @Mock
    LobbyStore lobbyStore;
    @Mock
    ChatManagement chatManagement;

    @Mock
    Chat defaultChat;
    @Mock
    ServerLobby defaultLobby;
    @Mock
    ServerUser defaultUser;

    UUID defaultLobbyID = new UUID(42, 0);
    UUID defaultChatID = new UUID(0, 42);

    @BeforeEach
    void init() {
        lobbyManagement = new LobbyManagement(lobbyStore, chatManagement);
    }

    // ################################################
    // createLobby(String, ServerUser)
    // ################################################

    @Test
    void createLobbyDefaultTest() {
        InOrder inOrder = inOrder(defaultLobby);
        when(lobbyStore.create("test")).thenReturn(defaultLobby);
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(chatManagement.createLobbyChat(defaultLobbyID)).thenReturn(defaultChat);
        when(defaultChat.getId()).thenReturn(defaultChatID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);

        ServerLobby lobby = lobbyManagement.createLobby("test", defaultUser);

        assertEquals(defaultLobby, lobby);
        verify(defaultLobby).joinUser(defaultUser);
        verify(chatManagement).createLobbyChat(defaultLobbyID);
        verify(chatManagement).addChatter(defaultChatID, defaultUser);
        inOrder.verify(defaultLobby).setChatID(defaultChatID);
        inOrder.verify(defaultLobby).getChatID();
        inOrder.verify(defaultLobby).getId();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void createLobbyNullTest() {
        assertThrows(NullPointerException.class, () -> lobbyManagement.createLobby(null, defaultUser));
        assertThrows(NullPointerException.class, () -> lobbyManagement.createLobby("test", null));
    }

    // ################################################
    // joinLobby(UUID, ServerUser)
    // ################################################

    @Test
    void joinLobbyDefaultTest() throws LobbyNotFoundException {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(java.util.Optional.ofNullable(defaultLobby));
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);

        ServerLobby lobby = lobbyManagement.joinLobby(defaultLobbyID, defaultUser);

        assertEquals(defaultLobby, lobby);
        verify(defaultLobby).joinUser(defaultUser);
        verify(chatManagement).addChatter(defaultChatID, defaultUser);
    }

    @Test
    void joinLobbyNullTest() {
        assertThrows(NullPointerException.class, () -> lobbyManagement.joinLobby(null, defaultUser));
        assertThrows(NullPointerException.class, () -> lobbyManagement.joinLobby(defaultLobbyID, null));
    }

    @Test
    void joinLobbyNotFoundTest() {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.empty());
        assertThrows(LobbyNotFoundException.class, () -> lobbyManagement.joinLobby(defaultLobbyID, defaultUser));
    }

    // ################################################
    // leaveLobby(UUID, ServerUser)
    // ################################################

    @Test
    void leaveLobbyDefaultTest() throws LobbyNotFoundException {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.of(defaultLobby));
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);
        when(defaultLobby.getUsers()).thenReturn(Collections.singletonList(defaultUser));

        assertFalse(lobbyManagement.leaveLobby(defaultLobbyID, defaultUser));

        verify(defaultLobby).leaveUser(defaultUser);
        verify(chatManagement).removeChatter(defaultChatID, defaultUser);
        verify(lobbyStore, never()).remove(defaultLobbyID);
        verify(chatManagement, never()).removeLobbyChat(defaultLobbyID);
    }

    @Test
    void leaveLobbyEmptyTest() throws LobbyNotFoundException {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.of(defaultLobby));
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);
        when(defaultLobby.getUsers()).thenReturn(Collections.emptyList());

        assertTrue(lobbyManagement.leaveLobby(defaultLobbyID, defaultUser));

        verify(defaultLobby).leaveUser(defaultUser);
        verify(chatManagement).removeChatter(defaultChatID, defaultUser);
        verify(lobbyStore).remove(defaultLobbyID);
        verify(chatManagement).removeLobbyChat(defaultLobbyID);
    }

    @Test
    void leaveLobbyNullTest() {
        assertThrows(NullPointerException.class, () -> lobbyManagement.leaveLobby(null, defaultUser));
        assertThrows(NullPointerException.class, () -> lobbyManagement.leaveLobby(defaultLobbyID, null));
    }

    @Test
    void leaveLobbyNotFoundTest() {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.empty());
        assertThrows(LobbyNotFoundException.class, () -> lobbyManagement.leaveLobby(defaultLobbyID, defaultUser));
    }

}
