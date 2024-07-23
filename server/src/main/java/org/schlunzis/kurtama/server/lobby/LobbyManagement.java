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
        lobby.setOwner(creator);
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

    /**
     * Leave a lobby. If the lobby is empty, it will be removed.
     *
     * @param lobbyID the id of the lobby
     * @param user    the user that wants to leave the lobby
     * @return true if the lobby was empty and was removed, false otherwise
     * @throws LobbyNotFoundException if the lobby was not found
     */
    public boolean leaveLobby(@NonNull UUID lobbyID, @NonNull ServerUser user) throws LobbyNotFoundException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            lobby.get().leaveUser(user);
            chatManagement.removeChatter(lobby.get().getChatID(), user);
            log.info("User {} left lobby with id: {}", user.getId(), lobby.get().getId());
            if (lobby.get().getUsers().isEmpty()) {
                lobbyStore.remove(lobbyID);
                chatManagement.removeLobbyChat(lobbyID);
                log.info("Lobby {} was empty and was removed.", lobbyID);
                return true;
            }
        } else {
            throw new LobbyNotFoundException();
        }
        return false;
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
