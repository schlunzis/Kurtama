package org.schlunzis.kurtama.server.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.common.UserDTO;
import org.schlunzis.kurtama.common.messages.chat.ChatMessageExceptionMessage;
import org.schlunzis.kurtama.common.messages.chat.ClientChatMessage;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    private static final UUID GLOBAL_CHAT_ID = new UUID(0, 0);

    ChatService chatService;

    @Mock
    ChatManagement chatManagement;
    @Mock
    Chat chat;
    @Mock
    List<ServerUser> chatters;

    @Mock
    ClientMessageContext<ClientChatMessage> cmc;
    @Mock
    ClientChatMessage ccm;

    @Mock
    UUID chatID;
    @Mock
    ServerUser serverUser;
    @Mock
    UserDTO userDTO;
    String nickname = "Strings cannot be mocked. Nickname";
    String message = "Strings cannot be mocked. Message";

    @Captor
    ArgumentCaptor<ChatMessageExceptionMessage> exceptionMessageArgumentCaptor;
    @Captor
    ArgumentCaptor<ServerChatMessage> serverChatMessageArgumentCaptor;
    @Captor
    ArgumentCaptor<List<ServerUser>> usersArgumentCaptor;

    @BeforeEach
    void init() {
        chatService = new ChatService(chatManagement);
        when(cmc.getClientMessage()).thenReturn(ccm);
    }

    @Test
    void onClientChatMessageGlobalDefaultTest() {
        when(ccm.getChatID()).thenReturn(GLOBAL_CHAT_ID);
        when(ccm.getNickname()).thenReturn(nickname);
        when(ccm.getMessage()).thenReturn(message);
        when(cmc.getUser()).thenReturn(serverUser);
        when(serverUser.toDTO()).thenReturn(userDTO);

        chatService.onClientChatMessage(cmc);

        verify(cmc).sendToAll(serverChatMessageArgumentCaptor.capture());
        ServerChatMessage serverChatMessage = serverChatMessageArgumentCaptor.getValue();
        assertEquals(GLOBAL_CHAT_ID, serverChatMessage.getChatID());
        assertEquals(nickname, serverChatMessage.getNickname());
        assertEquals(userDTO, serverChatMessage.getUser());
        assertEquals(message, serverChatMessage.getMessage());

        verify(cmc).close();
    }

    @Test
    void onClientChatMessageChatDefaultTest() {
        when(ccm.getChatID()).thenReturn(chatID);
        when(ccm.getNickname()).thenReturn(nickname);
        when(ccm.getMessage()).thenReturn(message);
        when(cmc.getUser()).thenReturn(serverUser);
        when(serverUser.toDTO()).thenReturn(userDTO);
        when(chatManagement.getChat(chatID)).thenReturn(Optional.of(chat));
        when(chat.getChatters()).thenReturn(chatters);

        chatService.onClientChatMessage(cmc);

        verify(cmc).sendToMany(serverChatMessageArgumentCaptor.capture(), usersArgumentCaptor.capture());
        ServerChatMessage serverChatMessage = serverChatMessageArgumentCaptor.getValue();
        assertEquals(chatID, serverChatMessage.getChatID());
        assertEquals(nickname, serverChatMessage.getNickname());
        assertEquals(userDTO, serverChatMessage.getUser());
        assertEquals(message, serverChatMessage.getMessage());
        List<ServerUser> users = usersArgumentCaptor.getValue();
        assertEquals(chatters, users);

        verify(cmc).close();
    }

    @Test
    void onClientChatMessageChatNonExistentTest() {
        when(ccm.getChatID()).thenReturn(chatID);
        when(ccm.getNickname()).thenReturn(nickname);
        when(ccm.getMessage()).thenReturn(message);
        when(cmc.getUser()).thenReturn(serverUser);
        when(serverUser.toDTO()).thenReturn(userDTO);
        when(chatManagement.getChat(chatID)).thenReturn(Optional.empty());

        chatService.onClientChatMessage(cmc);

        verify(cmc).respond(exceptionMessageArgumentCaptor.capture());
        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_CHAT_ID,
                exceptionMessageArgumentCaptor.getValue().errorCode());
        verify(cmc).close();
    }

    @Test
    void onClientChatMessageChatIDNullTest() {
        when(ccm.getChatID()).thenReturn(null);

        chatService.onClientChatMessage(cmc);

        verify(cmc).respond(exceptionMessageArgumentCaptor.capture());
        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_CHAT_ID,
                exceptionMessageArgumentCaptor.getValue().errorCode());
        verify(cmc).close();
    }

    @Test
    void onClientChatMessageNicknameNullTest() {
        when(ccm.getChatID()).thenReturn(chatID);
        when(ccm.getNickname()).thenReturn(null);

        chatService.onClientChatMessage(cmc);

        verify(cmc).respond(exceptionMessageArgumentCaptor.capture());
        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_NICKNAME,
                exceptionMessageArgumentCaptor.getValue().errorCode());
        verify(cmc).close();
    }

    @Test
    void onClientChatMessageNicknameBlankTest() {
        nickname = " ";

        when(ccm.getChatID()).thenReturn(chatID);
        when(ccm.getNickname()).thenReturn(nickname);

        chatService.onClientChatMessage(cmc);

        verify(cmc).respond(exceptionMessageArgumentCaptor.capture());
        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_NICKNAME,
                exceptionMessageArgumentCaptor.getValue().errorCode());
        verify(cmc).close();
    }

    @Test
    void onClientChatMessageMessageNullTest() {
        when(ccm.getChatID()).thenReturn(chatID);
        when(ccm.getNickname()).thenReturn(nickname);
        when(ccm.getMessage()).thenReturn(null);

        chatService.onClientChatMessage(cmc);

        verify(cmc).respond(exceptionMessageArgumentCaptor.capture());
        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_MESSAGE,
                exceptionMessageArgumentCaptor.getValue().errorCode());
        verify(cmc).close();
    }

    @Test
    void onClientChatMessageMessageBlankTest() {
        message = " ";

        when(ccm.getChatID()).thenReturn(chatID);
        when(ccm.getNickname()).thenReturn(nickname);
        when(ccm.getMessage()).thenReturn(message);

        chatService.onClientChatMessage(cmc);

        verify(cmc).respond(exceptionMessageArgumentCaptor.capture());
        assertEquals(ChatMessageExceptionMessage.ErrorCode.INVALID_MESSAGE,
                exceptionMessageArgumentCaptor.getValue().errorCode());
        verify(cmc).close();
    }

}
