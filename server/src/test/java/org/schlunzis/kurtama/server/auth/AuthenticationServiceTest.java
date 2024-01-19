package org.schlunzis.kurtama.server.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.common.UserDTO;
import org.schlunzis.kurtama.common.messages.IServerMessage;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginRequest;
import org.schlunzis.kurtama.common.messages.authentication.login.LoginSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutRequest;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterFailedResponse;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterRequest;
import org.schlunzis.kurtama.common.messages.authentication.register.RegisterSuccessfulResponse;
import org.schlunzis.kurtama.server.internal.ForcedLogoutEvent;
import org.schlunzis.kurtama.server.lobby.LobbyStore;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    AuthenticationService authenticationService;

    // common dependencies
    @Mock
    ApplicationEventPublisher eventBus;
    @Mock
    UserSessionMap userSessionMap;
    @Mock
    IUserStore userStore;
    @Mock
    LobbyStore lobbyStore;
    @Mock
    PasswordEncoder passwordEncoder;

    // user specific
    @Mock
    DBUser defaultDBUser;
    @Mock
    ServerUser defaultServerUser;
    @Mock
    UserDTO defaultUserDTO;
    String defaultUsername = "test";
    String defaultEmail = "test@schlunzis.org";
    String defaultPassword = "12345";
    String otherPassword = "12345";
    String defaultPasswordHash = "fancy hash";
    @Mock
    ISession defaultSession;

    // requests
    @Mock
    LoginRequest loginRequest;
    @Mock
    ClientMessageContext<LoginRequest> cmcLoginRequest;
    @Mock
    ClientMessageContext<LogoutRequest> cmcLogoutRequest;
    @Mock
    RegisterRequest registerRequest;
    @Mock
    ClientMessageContext<RegisterRequest> cmcRegisterRequest;

    // captors
    @Captor
    ArgumentCaptor<IServerMessage> serverMessageCaptor;
    @Captor
    ArgumentCaptor<ServerMessageWrapper> messageWrapperCaptor;
    @Captor
    ArgumentCaptor<DBUser> dbUserCaptor;

    @BeforeEach
    void init() {
        authenticationService = new AuthenticationService(eventBus, userSessionMap, userStore, lobbyStore, passwordEncoder);
    }

    // ################################################
    // onLoginEvent(ClientMessageContext<LoginRequest>)
    // ################################################

    @Test
    void onLoginEventUserDoesNotExistTest() {
        when(cmcLoginRequest.getClientMessage()).thenReturn(loginRequest);
        when(loginRequest.getEmail()).thenReturn(defaultEmail);
        when(userStore.getUser(defaultEmail)).thenReturn(Optional.empty());

        authenticationService.onLoginEvent(cmcLoginRequest);

        verify(userSessionMap, never()).put(any(), any());
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap, never()).remove((ISession) any());
        verify(cmcLoginRequest).respond(any(LoginFailedResponse.class));
        verify(cmcLoginRequest).closeWithReRequest();
        verify(cmcLoginRequest, never()).close();
    }

    @Test
    void onLoginEventWrongPasswordTest() {
        when(cmcLoginRequest.getClientMessage()).thenReturn(loginRequest);
        when(loginRequest.getEmail()).thenReturn(defaultEmail);
        when(userStore.getUser(defaultEmail)).thenReturn(Optional.of(defaultDBUser));
        when(loginRequest.getPassword()).thenReturn(defaultPassword);
        when(defaultDBUser.getPasswordHash()).thenReturn(defaultPasswordHash);
        when(passwordEncoder.matches(defaultPassword, defaultPasswordHash)).thenReturn(false);

        authenticationService.onLoginEvent(cmcLoginRequest);

        verify(userSessionMap, never()).put(any(), any());
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap, never()).remove((ISession) any());
        verify(cmcLoginRequest).respond(any(LoginFailedResponse.class));
        verify(cmcLoginRequest).closeWithReRequest();
        verify(cmcLoginRequest, never()).close();
    }

    @Test
    void onLoginEventSuccessfulFirstLoginTest() {
        when(cmcLoginRequest.getClientMessage()).thenReturn(loginRequest);
        when(loginRequest.getEmail()).thenReturn(defaultEmail);
        when(userStore.getUser(defaultEmail)).thenReturn(Optional.of(defaultDBUser));
        when(loginRequest.getPassword()).thenReturn(defaultPassword);
        when(defaultDBUser.getPasswordHash()).thenReturn(defaultPasswordHash);
        when(passwordEncoder.matches(defaultPassword, defaultPasswordHash)).thenReturn(true);
        when(defaultDBUser.toServerUser()).thenReturn(defaultServerUser);
        when(cmcLoginRequest.getSession()).thenReturn(defaultSession);
        when(defaultServerUser.toDTO()).thenReturn(defaultUserDTO);
        when(defaultDBUser.getEmail()).thenReturn(defaultEmail);
        when(userSessionMap.get(defaultServerUser)).thenReturn(Optional.empty());

        authenticationService.onLoginEvent(cmcLoginRequest);

        verify(userSessionMap).put(defaultServerUser, defaultSession);
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap, never()).remove((ISession) any());
        verify(cmcLoginRequest).respond(serverMessageCaptor.capture());
        assertEquals(1, serverMessageCaptor.getAllValues().size());
        IServerMessage message = serverMessageCaptor.getValue();
        assertInstanceOf(LoginSuccessfulResponse.class, message);
        LoginSuccessfulResponse response = (LoginSuccessfulResponse) message;
        assertEquals(defaultUserDTO, response.getUser());
        assertEquals(defaultEmail, response.getEmail());
        verify(lobbyStore).getAll();
        verify(cmcLoginRequest).closeWithReRequest();
        verify(cmcLoginRequest, never()).close();
    }

    @Test
    void onLoginEventSuccessfulSecondLoginTest() {
        when(cmcLoginRequest.getClientMessage()).thenReturn(loginRequest);
        when(loginRequest.getEmail()).thenReturn(defaultEmail);
        when(userStore.getUser(defaultEmail)).thenReturn(Optional.of(defaultDBUser));
        when(loginRequest.getPassword()).thenReturn(defaultPassword);
        when(defaultDBUser.getPasswordHash()).thenReturn(defaultPasswordHash);
        when(passwordEncoder.matches(defaultPassword, defaultPasswordHash)).thenReturn(true);
        when(defaultDBUser.toServerUser()).thenReturn(defaultServerUser);
        when(cmcLoginRequest.getSession()).thenReturn(defaultSession);
        when(defaultServerUser.toDTO()).thenReturn(defaultUserDTO);
        when(defaultDBUser.getEmail()).thenReturn(defaultEmail);
        when(userSessionMap.get(defaultServerUser)).thenReturn(Optional.empty());

        authenticationService.onLoginEvent(cmcLoginRequest);
        when(userSessionMap.get(defaultServerUser)).thenReturn(Optional.of(defaultSession));
        authenticationService.onLoginEvent(cmcLoginRequest);

        verify(userSessionMap).remove(defaultSession);
        verify(userSessionMap, times(2)).put(defaultServerUser, defaultSession);
        verify(cmcLoginRequest, times(2)).respond(serverMessageCaptor.capture());
        assertEquals(2, serverMessageCaptor.getAllValues().size());
        IServerMessage message = serverMessageCaptor.getValue();
        assertInstanceOf(LoginSuccessfulResponse.class, message);
        LoginSuccessfulResponse response = (LoginSuccessfulResponse) message;
        assertEquals(defaultUserDTO, response.getUser());
        assertEquals(defaultEmail, response.getEmail());
        verify(lobbyStore, times(2)).getAll();
        verify(cmcLoginRequest, times(2)).closeWithReRequest();
        verify(cmcLoginRequest, never()).close();
        verify(eventBus).publishEvent(messageWrapperCaptor.capture());
        ServerMessageWrapper wrapper = messageWrapperCaptor.getValue();
        assertInstanceOf(LogoutSuccessfulResponse.class, wrapper.getServerMessage());
        assertEquals(1, wrapper.getRecipients().size());
        assertTrue(wrapper.getRecipients().contains(defaultSession));
    }

    @Test
    void onLoginEventUnsuccessfulSecondLoginTest() {
        when(cmcLoginRequest.getClientMessage()).thenReturn(loginRequest);
        when(loginRequest.getEmail()).thenReturn(defaultEmail);
        when(userStore.getUser(defaultEmail)).thenReturn(Optional.of(defaultDBUser));
        when(loginRequest.getPassword()).thenReturn(defaultPassword);
        when(defaultDBUser.getPasswordHash()).thenReturn(defaultPasswordHash);
        when(passwordEncoder.matches(defaultPassword, defaultPasswordHash)).thenReturn(true);
        when(defaultDBUser.toServerUser()).thenReturn(defaultServerUser);
        when(cmcLoginRequest.getSession()).thenReturn(defaultSession);
        when(defaultServerUser.toDTO()).thenReturn(defaultUserDTO);
        when(defaultDBUser.getEmail()).thenReturn(defaultEmail);
        when(userSessionMap.get(defaultServerUser)).thenReturn(Optional.empty());

        authenticationService.onLoginEvent(cmcLoginRequest);
        when(loginRequest.getPassword()).thenReturn(otherPassword);
        when(passwordEncoder.matches(otherPassword, defaultPasswordHash)).thenReturn(false);
        authenticationService.onLoginEvent(cmcLoginRequest);

        verify(userSessionMap).put(defaultServerUser, defaultSession);
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap, never()).remove((ISession) any());
        verify(cmcLoginRequest).respond(any(LoginSuccessfulResponse.class));
        verify(cmcLoginRequest).respond(any(LoginFailedResponse.class));
        verify(lobbyStore).getAll();
        verify(cmcLoginRequest, times(2)).closeWithReRequest();
        verify(cmcLoginRequest, never()).close();
    }

    // ################################################
    // onLogoutRequest(ClientMessageContext<LogoutRequest>)
    // ################################################

    @Test
    void onLogoutRequestTest() {
        when(cmcLogoutRequest.getSession()).thenReturn(defaultSession);

        authenticationService.onLogoutRequest(cmcLogoutRequest);

        verify(userSessionMap, never()).put(any(), any());
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap).remove(defaultSession);
        verify(eventBus).publishEvent(messageWrapperCaptor.capture());
        ServerMessageWrapper wrapper = messageWrapperCaptor.getValue();
        assertInstanceOf(LogoutSuccessfulResponse.class, wrapper.getServerMessage());
        assertEquals(1, wrapper.getRecipients().size());
        assertTrue(wrapper.getRecipients().contains(defaultSession));
        verify(cmcLoginRequest, never()).close();
        verify(cmcLoginRequest, never()).closeWithReRequest();
    }

    // ################################################
    // onRegisterEvent(ClientMessageContext<RegisterRequest>)
    // ################################################

    @Test
    void onRegisterEventSuccessfulTest() {
        when(cmcRegisterRequest.getClientMessage()).thenReturn(registerRequest);
        when(registerRequest.getEmail()).thenReturn(defaultEmail);
        when(registerRequest.getUsername()).thenReturn(defaultUsername);
        when(registerRequest.getPassword()).thenReturn(defaultPassword);

        authenticationService.onRegisterEvent(cmcRegisterRequest);

        verify(userStore).createUser(dbUserCaptor.capture());
        DBUser user = dbUserCaptor.getValue();
        assertEquals(defaultEmail, user.getEmail());
        assertEquals(defaultUsername, user.getUsername());
        assertEquals(defaultPassword, user.getPasswordHash());
        verify(cmcRegisterRequest).respond(any(RegisterSuccessfulResponse.class));
        verify(userSessionMap, never()).put(any(), any());
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap, never()).remove((ISession) any());
        verify(cmcRegisterRequest).close();
        verify(cmcRegisterRequest, never()).closeWithReRequest();
    }

    @Test
    void onRegisterEventDuplicateEmailTest() {
        when(cmcRegisterRequest.getClientMessage()).thenReturn(registerRequest);
        when(registerRequest.getEmail()).thenReturn(defaultEmail);
        when(registerRequest.getUsername()).thenReturn(defaultUsername);
        when(registerRequest.getPassword()).thenReturn(defaultPassword);
        when(userStore.createUser(any())).thenThrow(IllegalArgumentException.class);

        authenticationService.onRegisterEvent(cmcRegisterRequest);

        verify(cmcRegisterRequest).respond(any(RegisterFailedResponse.class));
        verify(userSessionMap, never()).put(any(), any());
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap, never()).remove((ISession) any());
        verify(cmcRegisterRequest).close();
        verify(cmcRegisterRequest, never()).closeWithReRequest();
    }

    // ################################################
    // onForcedLogoutEvent(ForcedLogoutEvent)
    // ################################################

    @Test
    void onForcedLogoutEventTest() {
        ForcedLogoutEvent forcedLogoutEvent = mock(ForcedLogoutEvent.class);
        when(forcedLogoutEvent.session()).thenReturn(defaultSession);

        authenticationService.onForcedLogoutEvent(forcedLogoutEvent);

        verify(userSessionMap, never()).put(any(), any());
        verify(userSessionMap, never()).remove((ServerUser) any());
        verify(userSessionMap).remove(defaultSession);
        verify(eventBus).publishEvent(messageWrapperCaptor.capture());
        ServerMessageWrapper wrapper = messageWrapperCaptor.getValue();
        assertInstanceOf(LogoutSuccessfulResponse.class, wrapper.getServerMessage());
        assertEquals(1, wrapper.getRecipients().size());
        assertTrue(wrapper.getRecipients().contains(defaultSession));
    }

}
