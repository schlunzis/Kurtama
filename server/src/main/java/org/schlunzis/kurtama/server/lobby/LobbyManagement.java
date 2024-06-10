package org.schlunzis.kurtama.server.lobby;

import lombok.NonNull;
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

    public ServerLobby createLobby(@NonNull String lobbyName, @NonNull ServerUser creator) {
        ServerLobby lobby = lobbyStore.create(lobbyName);
        log.info("Created lobby with name: {} and id: {}", lobby.getName(), lobby.getId());

        Chat chat = chatManagement.createLobbyChat(lobby.getId());
        lobby.setChatID(chat.getId());
        joinLobby(lobby, creator);
        return lobby;
    }

    public ServerLobby joinLobby(@NonNull UUID lobbyID, @NonNull ServerUser user) throws LobbyNotFoundException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            joinLobby(lobby.get(), user);
            return lobby.get();
        } else {
            throw new LobbyNotFoundException();
        }
    }

    public void leaveLobby(@NonNull UUID lobbyID, @NonNull ServerUser user) throws LobbyNotFoundException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            lobby.get().leaveUser(user);
            chatManagement.removeChatter(lobby.get().getChatID(), user);
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

    public ServerLobby getLobby(UUID lobbyID) throws LobbyNotFoundException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            return lobby.get();
        } else {
            throw new LobbyNotFoundException();
        }
    }

    public Collection<ServerLobby> getAll() {
        return lobbyStore.getAll();
    }

    private void joinLobby(ServerLobby lobby, ServerUser user) {
        lobby.joinUser(user);
        chatManagement.addChatter(lobby.getChatID(), user);
        log.info("User {} joined lobby with id: {}", user.getId(), lobby.getId());
    }

}
