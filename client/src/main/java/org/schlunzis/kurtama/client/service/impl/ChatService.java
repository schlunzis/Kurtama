package org.schlunzis.kurtama.client.service.impl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.service.IChatService;
import org.schlunzis.kurtama.client.service.ISessionService;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.messages.authentication.logout.LogoutSuccessfulResponse;
import org.schlunzis.kurtama.common.messages.chat.ClientChatMessage;
import org.schlunzis.kurtama.common.messages.chat.ServerChatMessage;
import org.schlunzis.kurtama.common.messages.lobby.server.JoinLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LeaveLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LobbyCreatedSuccessfullyResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {

    private static final UUID GLOBAL_CHAT_ID = new UUID(0, 0);

    private final ISessionService mainService;
    private final ApplicationEventPublisher eventBus;

    private final ObservableList<String> chatMessages = FXCollections.observableList(new ArrayList<>());
    private volatile UUID currentChatID = GLOBAL_CHAT_ID;

    @EventListener
    public void onLogoutSuccessfulResponse(LogoutSuccessfulResponse lsr) {
        currentChatID = GLOBAL_CHAT_ID;
    }

    @EventListener
    public void onLobbyCreatedSuccessfullyResponse(LobbyCreatedSuccessfullyResponse lcsr) {
        currentChatID = lcsr.lobby().getChatID();
    }

    @EventListener
    public void onJoinLobbySuccessfullyResponse(JoinLobbySuccessfullyResponse jlsr) {
        currentChatID = jlsr.lobby().getChatID();
    }

    @EventListener
    public void onLeaveLobbySuccessfullyResponse(LeaveLobbySuccessfullyResponse llsr) {
        currentChatID = GLOBAL_CHAT_ID;
    }

    @EventListener
    public void onServerChatMessage(ServerChatMessage message) {
        if (message.getChatID().equals(currentChatID)) {
            Platform.runLater(() -> chatMessages.add(message.getNickname() + ": " + message.getMessage()));
        }
    }

    public void sendMessage(String nickname, String message) {
        eventBus.publishEvent(new ClientChatMessage(currentChatID, nickname, message));
    }

    public String getCurrentUsername() {
        return mainService.getCurrentUser().map(IUser::getUsername).orElse("John Doe");
    }

}
