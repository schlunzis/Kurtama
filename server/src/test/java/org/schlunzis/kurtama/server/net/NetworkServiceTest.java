package org.schlunzis.kurtama.server.net;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.server.service.ServerMessageWrapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NetworkServiceTest {

    NetworkService networkService;

    @Mock
    INetworkServer networkServer;

    @Mock
    IServerMessage serverMessage;

    @BeforeEach
    void init() {
        networkService = new NetworkService();
    }

    // ################################################
    // addServer(SessionType, INetworkServer)
    // ################################################

    @Test
    void addServerNullTypeTest() {
        assertThrows(NullPointerException.class, () -> networkService.addServer(null, networkServer));
    }

    @Test
    void addServerNullServerTest() {
        assertThrows(NullPointerException.class, () -> networkService.addServer(SessionType.NETTY, null));
    }

    @Test
    void addServerExpectedTest() {
        Optional<INetworkServer> optionalPreviousServer = networkService.addServer(SessionType.NETTY, networkServer);
        assertTrue(optionalPreviousServer.isEmpty());
    }

    @Test
    void addServerRepeatedTest(@Mock INetworkServer otherNetworkServer) {
        Optional<INetworkServer> optionalPreviousServer = networkService.addServer(SessionType.NETTY, networkServer);
        assertTrue(optionalPreviousServer.isEmpty());
        optionalPreviousServer = networkService.addServer(SessionType.NETTY, otherNetworkServer);
        assertTrue(optionalPreviousServer.isPresent());
        assertEquals(networkServer, optionalPreviousServer.get());
    }

    // ################################################
    // onServerMessage(IServerMessage)
    // ################################################

    @Test
    void onServerMessageExpectedTest() {
        networkService.addServer(SessionType.NETTY, networkServer);
        networkService.onServerMessage(serverMessage);
        verify(networkServer).sendMessage(serverMessage);
    }

    @Test
    void onServerMessageNullTest() {
        networkService.addServer(SessionType.NETTY, networkServer);
        assertDoesNotThrow(() -> networkService.onServerMessage(null));
        verify(networkServer, never()).sendMessage(any());
    }

    // ################################################
    // onMessageWrapper(ServerMessageWrapper)
    // ################################################

    @Test
    void onWrapperExpectedTest(@Mock ISession session, @Mock ISession session2, @Mock UUID sessionID,
                               @Captor ArgumentCaptor<Collection<ISession>> sessionsCaptor) {
        networkService.addServer(SessionType.NETTY, networkServer);
        when(session.sessionType()).thenReturn(SessionType.NETTY);
        when(session.id()).thenReturn(sessionID);
        when(session2.sessionType()).thenReturn(SessionType.NETTY);
        when(session2.id()).thenReturn(sessionID);
        ServerMessageWrapper wrapper = new ServerMessageWrapper(serverMessage, session, session2);

        networkService.onMessageWrapper(wrapper);

        verify(networkServer).sendMessage(eq(serverMessage), sessionsCaptor.capture());
        Collection<ISession> sessions = sessionsCaptor.getValue();
        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(session));
        assertTrue(sessions.contains(session2));
    }

    @Test
    void onWrapperNullTest() {
        networkService.addServer(SessionType.NETTY, networkServer);
        assertDoesNotThrow(() -> networkService.onMessageWrapper(null));
        verify(networkServer, never()).sendMessage(any(), any());
    }

    @Test
    void onWrapperMessageNullTest(@Mock ISession session) {
        networkService.addServer(SessionType.NETTY, networkServer);
        ServerMessageWrapper wrapper = new ServerMessageWrapper(null, session);
        assertDoesNotThrow(() -> networkService.onMessageWrapper(wrapper));
        verify(networkServer, never()).sendMessage(any(), any());
    }

    @Test
    void onWrapperSessionNullTest() {
        networkService.addServer(SessionType.NETTY, networkServer);
        ServerMessageWrapper wrapper = new ServerMessageWrapper(serverMessage, Collections.singleton(null));
        assertDoesNotThrow(() -> networkService.onMessageWrapper(wrapper));
        verify(networkServer, never()).sendMessage(any(), any());
    }

    @Test
    void onWrapperNoSessionsTest() {
        networkService.addServer(SessionType.NETTY, networkServer);
        ServerMessageWrapper wrapper = new ServerMessageWrapper(serverMessage, Collections.emptyList());
        assertDoesNotThrow(() -> networkService.onMessageWrapper(wrapper));
        verify(networkServer, never()).sendMessage(any(), any());
    }

}
