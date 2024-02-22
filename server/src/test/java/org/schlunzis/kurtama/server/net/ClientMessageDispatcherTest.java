package org.schlunzis.kurtama.server.net;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.server.auth.IAuthenticationService;
import org.schlunzis.kurtama.server.internal.ForcedLogoutEvent;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientMessageDispatcherTest {

    ClientMessageDispatcher clientMessageDispatcher;

    @Mock
    IAuthenticationService authenticationService;
    @Mock
    ApplicationEventPublisher eventBus;

    @Mock
    IClientMessage clientMessage;
    @Mock
    ISession session;

    @Captor
    ArgumentCaptor<ClientMessageContext<IClientMessage>> contextCaptor;

    @BeforeEach
    void init() {
        clientMessageDispatcher = new ClientMessageDispatcher(authenticationService, eventBus);
    }

    // ################################################
    // dispatch(IClientMessage, ISession)
    // ################################################

    @Test
    void dispatchNullTest() {
        assertThrows(NullPointerException.class, () -> clientMessageDispatcher.dispatch(null, session));
        assertThrows(NullPointerException.class, () -> clientMessageDispatcher.dispatch(clientMessage, null));
    }

    @Test
    void dispatchExpectedLoginRequestTest(@Mock LoginRequest loginRequest) {
        clientMessageDispatcher.dispatch(loginRequest, session);

        verify(eventBus).publishEvent(contextCaptor.capture());
        ClientMessageContext<IClientMessage> context = contextCaptor.getValue();
        verifyPublishedContext(context, loginRequest, session, null);
    }

    @Test
    void dispatchExpectedRegisterRequestTest(@Mock RegisterRequest registerRequest) {
        clientMessageDispatcher.dispatch(registerRequest, session);

        verify(eventBus).publishEvent(contextCaptor.capture());
        ClientMessageContext<IClientMessage> context = contextCaptor.getValue();
        verifyPublishedContext(context, registerRequest, session, null);
    }

    @Test
    void dispatchExpectedUnauthenticatedTest() {
        when(authenticationService.getUserForSession(session)).thenReturn(Optional.empty());

        clientMessageDispatcher.dispatch(clientMessage, session);

        verify(eventBus, never()).publishEvent(any());
    }

    @Test
    void dispatchExpectedAuthenticatedTest(@Mock ServerUser user) {
        when(authenticationService.getUserForSession(session)).thenReturn(Optional.of(user));

        clientMessageDispatcher.dispatch(clientMessage, session);

        verify(eventBus).publishEvent(contextCaptor.capture());
        ClientMessageContext<IClientMessage> context = contextCaptor.getValue();
        verifyPublishedContext(context, clientMessage, session, user);
    }

    // ################################################
    // clientDisconnected(ISession)
    // ################################################

    @Test
    void clientDisconnectedNullTest() {
        assertThrows(NullPointerException.class, () -> clientMessageDispatcher.clientDisconnected(null));
    }

    @Test
    void clientDisconnectedExpectedTest(@Captor ArgumentCaptor<ForcedLogoutEvent> captor) {
        clientMessageDispatcher.clientDisconnected(session);

        verify(eventBus).publishEvent(captor.capture());
        ForcedLogoutEvent event = captor.getValue();
        assertEquals(session, event.session());
    }

    // ################################################
    // internal
    // ################################################

    private void verifyPublishedContext(ClientMessageContext<IClientMessage> context, IClientMessage clientMessage, ISession session, ServerUser user) {
        assertEquals(clientMessage, context.getClientMessage());
        assertEquals(session, context.getSession());
        assertEquals(user, context.getUser());
    }

}
