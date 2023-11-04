package org.schlunzis.kurtama.server.lobby;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.LobbyDTO;
import org.schlunzis.kurtama.common.messages.lobby.client.CreateLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.JoinLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.LeaveLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.server.*;
import org.schlunzis.kurtama.server.auth.UserSessionMap;
import org.schlunzis.kurtama.server.net.ClientMessageWrapper;
import org.schlunzis.kurtama.server.net.ServerMessageWrapper;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyStore lobbyStore;
    private final ApplicationEventPublisher eventBus;
    private final UserSessionMap userSessionMap;

    @EventListener
    public void onCreateLobbyRequest(ClientMessageWrapper<CreateLobbyRequest> cmw) {
        CreateLobbyRequest request = cmw.clientMessage();

        Optional<ServerUser> user = userSessionMap.get(cmw.session());
        if (user.isPresent()) {
            ServerLobby lobby = lobbyStore.create(request.name());
            log.info("Created lobby with name: {} and id: {}", lobby.getName(), lobby.getId());
            lobby.joinUser(user.get());
            log.info("User {} joined lobby with id: {}", user.get().getId(), lobby.getId());
            eventBus.publishEvent(new ServerMessageWrapper(new LobbyCreatedSuccessfullyResponse(lobby.toDTO()), cmw.session()));
            updateLobbyListInfo();
        } else {
            log.info("Could not create lobby. No user found for session.");
            eventBus.publishEvent(new ServerMessageWrapper(new LobbyCreationFailedResponse(), cmw.session()));
        }
    }

    @EventListener
    public void onJoinLobbyRequest(ClientMessageWrapper<JoinLobbyRequest> cmw) {
        JoinLobbyRequest request = cmw.clientMessage();

        Optional<ServerUser> user = userSessionMap.get(cmw.session());
        if (user.isPresent()) {
            ServerLobby lobby = lobbyStore.get(request.lobbyID());
            lobby.joinUser(user.get());
            log.info("User {} joined lobby with id: {}", user.get().getId(), lobby.getId());
            eventBus.publishEvent(new ServerMessageWrapper(new JoinLobbySuccessfullyResponse(), cmw.session()));
            // TODO update clients of users in lobby #8
            updateLobbyListInfo();
        } else {
            log.info("Could not join lobby. No user found for session.");
            eventBus.publishEvent(new ServerMessageWrapper(new JoinLobbyFailedResponse(), cmw.session()));
        }
    }

    @EventListener
    public void onLeaveLobbyRequest(ClientMessageWrapper<LeaveLobbyRequest> cmw) {
        LeaveLobbyRequest request = cmw.clientMessage();

        Optional<ServerUser> user = userSessionMap.get(cmw.session());
        if (user.isPresent()) {
            ServerLobby lobby = lobbyStore.get(request.lobbyID());
            lobby.leaveUser(user.get());
            log.info("User {} left lobby with id: {}", user.get().getId(), lobby.getId());
            eventBus.publishEvent(new ServerMessageWrapper(new LeaveLobbySuccessfullyResponse(), cmw.session()));
            // TODO update clients of users in lobby #8
            // TODO delete lobby if empty
            updateLobbyListInfo();
        } else {
            log.info("Could not leave lobby. No user found for session.");
            eventBus.publishEvent(new ServerMessageWrapper(new LobbyCreationFailedResponse(), cmw.session()));
        }
    }

    private void updateLobbyListInfo() {
        Collection<LobbyDTO> lobbyDTOS = lobbyStore.getAll().stream().map(ServerLobby::toDTO).toList();
        eventBus.publishEvent(new ServerMessageWrapper(new LobbyListInfoMessage(lobbyDTOS), userSessionMap.getAllSessions()));
    }

}
