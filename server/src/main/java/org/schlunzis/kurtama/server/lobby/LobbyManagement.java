package org.schlunzis.kurtama.server.lobby;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.chat.Chat;
import org.schlunzis.kurtama.server.chat.ChatManagement;
import org.schlunzis.kurtama.server.lobby.exception.LobbyNotFoundException;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LobbyManagement {

    private final LobbyStore lobbyStore;
    private final ChatManagement chatManagement;

    public ServerLobby createLobby(String lobbyName, ServerUser creator) {
        ServerLobby lobby = lobbyStore.create(lobbyName);
        log.info("Created lobby with name: {} and id: {}", lobby.getName(), lobby.getId());

        lobby.joinUser(creator);
        log.info("User {} joined lobby with id: {}", creator.getId(), lobby.getId());

        Chat chat = chatManagement.createLobbyChat(lobby.getId());
        lobby.setChatID(chat.getId());

        return lobby;
    }

    public ServerLobby joinLobby(UUID lobbyID, ServerUser user) throws LobbyNotFoundException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            lobby.get().joinUser(user);
            log.info("User {} joined lobby with id: {}", user.getId(), lobby.get().getId());
            return lobby.get();
        } else {
            throw new LobbyNotFoundException();
        }
    }

    public void leaveLobby(UUID lobbyID, ServerUser user) throws LobbyNotFoundException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            lobby.get().leaveUser(user);
            log.info("User {} left lobby with id: {}", user.getId(), lobby.get().getId());
            if (lobby.get().getUsers().isEmpty()) {
                lobbyStore.remove(lobbyID);
                chatManagement.removeLobbyChat(lobbyID);
                log.info("Lobby {} was empty and was removed.", lobbyID);
            }
        } else {
            throw new LobbyNotFoundException();
        }
    }

    public Collection<ServerLobby> getAll() {
        return lobbyStore.getAll();
    }

}
