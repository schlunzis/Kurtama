package org.schlunzis.kurtama.server.lobby;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.common.messages.IClientMessage;
import org.schlunzis.kurtama.common.messages.lobby.client.CreateLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.JoinLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.client.LeaveLobbyRequest;
import org.schlunzis.kurtama.common.messages.lobby.server.*;
import org.schlunzis.kurtama.server.lobby.exception.LobbyNotFoundException;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyManagement lobbyManagement;

    @EventListener
    public void onCreateLobbyRequest(ClientMessageContext<CreateLobbyRequest> cmc) {
        CreateLobbyRequest request = cmc.getClientMessage();
        try {
            ServerLobby lobby = lobbyManagement.createLobby(request.name(), cmc.getUser());
            cmc.respond(new LobbyCreatedSuccessfullyResponse(lobby.toDTO()));
            updateLobbyListInfo(cmc);
        } catch (Exception e) {
            log.info("Could not create lobby. No user found for session.");
            cmc.respond(new LobbyCreationFailedResponse());
        }
        cmc.close();
    }

    @EventListener
    public void onJoinLobbyRequest(ClientMessageContext<JoinLobbyRequest> cmc) {
        JoinLobbyRequest request = cmc.getClientMessage();

        try {
            ServerLobby lobby = lobbyManagement.joinLobby(request.lobbyID(), cmc.getUser());
            cmc.respond(new JoinLobbySuccessfullyResponse(lobby.toDTO()));
            // TODO update clients of users in lobby #8
            updateLobbyListInfo(cmc);
        } catch (LobbyNotFoundException e) {
            log.info("Could not join lobby. Lobby not found.");
            cmc.respond(new JoinLobbyFailedResponse());
        } catch (Exception e) {
            log.error("Could not join lobby.", e);
            cmc.respond(new JoinLobbyFailedResponse());
        }
        cmc.close();
    }

    @EventListener
    public void onLeaveLobbyRequest(ClientMessageContext<LeaveLobbyRequest> cmc) {
        LeaveLobbyRequest request = cmc.getClientMessage();
        try {
            lobbyManagement.leaveLobby(request.lobbyID(), cmc.getUser());
            cmc.respond(new LeaveLobbySuccessfullyResponse());
            // TODO update clients of users in lobby #8
            updateLobbyListInfo(cmc);
        } catch (LobbyNotFoundException e) {
            log.info("Could not leave lobby. Lobby not found.");
            cmc.respond(new LobbyLeaveFailedResponse());
        } catch (Exception e) {
            log.error("Could not leave lobby.", e);
            cmc.respond(new LobbyLeaveFailedResponse());
        }
        cmc.close();
    }

    private void updateLobbyListInfo(ClientMessageContext<? extends IClientMessage> cmc) {
        Collection<LobbyInfo> lobbyInfos = lobbyManagement.getAll().stream().map(ServerLobby::getInfo).toList();
        cmc.sendToAll(new LobbyListInfoMessage(lobbyInfos));
    }

}
