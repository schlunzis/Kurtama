package org.schlunzis.kurtama.server.lobby;

import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.lobby.client.CreateLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.JoinLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.LeaveLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.server.JoinLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LeaveLobbySuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LobbyCreatedSuccessfullyResponse;
import org.schlunzis.kurtama.common.messages.lobby.server.LobbyListInfoMessage;
import org.schlunzis.kurtama.server.auth.AuthenticationService;
import org.schlunzis.kurtama.server.net.ClientMessageWrapper;
import org.schlunzis.kurtama.server.service.AbstractService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class LobbyService extends AbstractService {

    private final LobbyManagement lobbyManagement;

    public LobbyService(ApplicationEventPublisher eventBus, AuthenticationService authenticationService, LobbyManagement lobbyManagement) {
        super(eventBus, authenticationService);
        this.lobbyManagement = lobbyManagement;
    }

    @EventListener
    public void onCreateLobbyRequest(ClientMessageWrapper<CreateLobbyRequest> cmw) {
        CreateLobbyRequest request = cmw.clientMessage();

        ServerLobby lobby = lobbyManagement.createLobby(request.name(), user.get());
        sendTo(new LobbyCreatedSuccessfullyResponse(lobby.toDTO()), cmw.user());
        updateLobbyListInfo();

        // TODO exceptions?
        //log.info("Could not create lobby. No user found for session.");
        //sendTo(new LobbyCreationFailedResponse(), cmw.session());
    }

    @EventListener
    public void onJoinLobbyRequest(ClientMessageWrapper<JoinLobbyRequest> cmw) {
        JoinLobbyRequest request = cmw.clientMessage();

        ServerLobby lobby = lobbyManagement.joinLobby(request.lobbyID(), user.get());
        sendTo(new JoinLobbySuccessfullyResponse(lobby.toDTO()), cmw.user());
        // TODO update clients of users in lobby #8
        updateLobbyListInfo();

        // TODO exceptions?
        //log.info("Could not join lobby. No user found for session.");
        //sendTo(new JoinLobbyFailedResponse(), cmw.session());
    }

    @EventListener
    public void onLeaveLobbyRequest(ClientMessageWrapper<LeaveLobbyRequest> cmw) {
        LeaveLobbyRequest request = cmw.clientMessage();

        lobbyManagement.leaveLobby(request.lobbyID(), user.get());
        sendTo(new LeaveLobbySuccessfullyResponse(), cmw.user());
        // TODO update clients of users in lobby #8
        updateLobbyListInfo();

        //TODO exceptions?
        //log.info("Could not leave lobby. No user found for session.");
        //sendTo(new LobbyCreationFailedResponse(), cmw.session());
    }

    private void updateLobbyListInfo() {
        Collection<LobbyInfo> lobbyInfos = lobbyManagement.getAll().stream().map(ServerLobby::getInfo).toList();
        sendToAll(new LobbyListInfoMessage(lobbyInfos));
    }

}
