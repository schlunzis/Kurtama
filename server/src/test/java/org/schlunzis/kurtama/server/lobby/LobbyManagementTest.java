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
import org.schlunzis.kurtama.server.lobby.exception.WrongLobbyPasswordException;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    PasswordEncoder passwordEncoder;

    String defaultPassword = "12345";
    String otherPassword = "6789";
    String defaultPasswordHash = "fancy hash";


    @BeforeEach
    void init() {
        lobbyManagement = new LobbyManagement(lobbyStore, chatManagement, passwordEncoder);
    }

    // ################################################
    // createLobby(String, ServerUser)
    // ################################################

    @Test
    void createLobbyDefaultTest() {
        InOrder inOrder = inOrder(defaultLobby);
        when(lobbyStore.create("test", defaultPasswordHash)).thenReturn(defaultLobby);
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(chatManagement.createLobbyChat(defaultLobbyID)).thenReturn(defaultChat);
        when(defaultChat.getId()).thenReturn(defaultChatID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);
        when(passwordEncoder.encode(defaultPassword)).thenReturn(defaultPasswordHash);

        ServerLobby lobby = lobbyManagement.createLobby("test", defaultPassword, defaultUser);

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
        assertThrows(NullPointerException.class, () -> lobbyManagement.createLobby(null, defaultPassword, defaultUser));
        assertThrows(NullPointerException.class, () -> lobbyManagement.createLobby("test", defaultPassword, null));
    }

    // ################################################
    // joinLobby(UUID, ServerUser)
    // ################################################

    @Test
    void joinLobbyDefaultTest() throws LobbyNotFoundException, WrongLobbyPasswordException {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.ofNullable(defaultLobby));
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);
        when(defaultLobby.isPasswordProtected()).thenReturn(false);

        ServerLobby lobby = lobbyManagement.joinLobby(defaultLobbyID, "", defaultUser);

        assertEquals(defaultLobby, lobby);
        verify(defaultLobby).joinUser(defaultUser);
        verify(chatManagement).addChatter(defaultChatID, defaultUser);
    }

    @Test
    void joinLobbyPasswordProtected() throws LobbyNotFoundException, WrongLobbyPasswordException {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.ofNullable(defaultLobby));
        when(defaultLobby.getId()).thenReturn(defaultLobbyID);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);
        when(defaultLobby.isPasswordProtected()).thenReturn(true);
        when(defaultLobby.getPasswordHash()).thenReturn(defaultPasswordHash);
        when(passwordEncoder.matches(defaultPassword, defaultPasswordHash)).thenReturn(true);

        ServerLobby lobby = lobbyManagement.joinLobby(defaultLobbyID, defaultPassword, defaultUser);

        assertEquals(defaultLobby, lobby);
        verify(defaultLobby).joinUser(defaultUser);
        verify(chatManagement).addChatter(defaultChatID, defaultUser);
    }


    @Test
    void joinLobbyNullTest() {
        assertThrows(NullPointerException.class, () -> lobbyManagement.joinLobby(null, defaultPassword, defaultUser));
        assertThrows(NullPointerException.class, () -> lobbyManagement.joinLobby(defaultLobbyID, defaultPassword, null));
    }

    @Test
    void joinLobbyNotFoundTest() {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.empty());
        assertThrows(LobbyNotFoundException.class, () -> lobbyManagement.joinLobby(defaultLobbyID, defaultPassword, defaultUser));
    }

    @Test
    void joinLobbyWrongPasswordTest() {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.of(defaultLobby));
        when(defaultLobby.isPasswordProtected()).thenReturn(true);
        when(defaultLobby.getPasswordHash()).thenReturn(defaultPasswordHash);
        when(passwordEncoder.matches(otherPassword, defaultPasswordHash)).thenReturn(false);
        assertThrows(WrongLobbyPasswordException.class, () -> lobbyManagement.joinLobby(defaultLobbyID, otherPassword, defaultUser));
    }

    @Test
    void joinLobbyNoPasswordTest() throws LobbyNotFoundException, WrongLobbyPasswordException {
        when(lobbyStore.get(defaultLobbyID)).thenReturn(Optional.of(defaultLobby));
        when(defaultLobby.isPasswordProtected()).thenReturn(false);
        when(defaultLobby.getChatID()).thenReturn(defaultChatID);

        ServerLobby lobby = lobbyManagement.joinLobby(defaultLobbyID, otherPassword, defaultUser);

        assertEquals(defaultLobby, lobby);
        verify(defaultLobby).joinUser(defaultUser);
        verify(chatManagement).addChatter(defaultChatID, defaultUser);
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
