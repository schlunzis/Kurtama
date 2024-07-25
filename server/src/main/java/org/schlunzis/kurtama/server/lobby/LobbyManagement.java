package org.schlunzis.kurtama.server.lobby;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.chat.Chat;
import org.schlunzis.kurtama.server.chat.ChatManagement;
import org.schlunzis.kurtama.server.lobby.exception.LobbyNotFoundException;
import org.schlunzis.kurtama.server.lobby.exception.WrongLobbyPasswordException;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    /**
     * Create a new lobby with a random UUID. The creator will be added to the lobby. In this method the password is
     * hashed. If the password is empty, the hash will be empty as well. Starting from here, the server will only work
     * with hashed passwords.
     * <p>
     * The chat for the lobby will be created as well.
     *
     * @param lobbyName     the lobby's name
     * @param lobbyPassword the lobby's raw password
     * @param creator       the user that creates the lobby
     * @return the created lobby
     */
    public ServerLobby createLobby(@NonNull String lobbyName, @NonNull String lobbyPassword, @NonNull ServerUser creator) {
        String passwordHash = lobbyPassword.isBlank() ? "" : passwordEncoder.encode(lobbyPassword);

        ServerLobby lobby = lobbyStore.create(lobbyName, passwordHash);
        log.info("Created lobby with name: {} and id: {}", lobby.getName(), lobby.getId());

        Chat chat = chatManagement.createLobbyChat(lobby.getId());
        lobby.setChatID(chat.getId());
        lobby.setOwner(creator);
        joinLobby(lobby, creator);
        return lobby;
    }

    /**
     * Join a lobby. If the lobby is password protected, the password will be checked. If the password is correct, the user
     * will be added to the lobby.
     *
     * @param lobbyID  the id of the lobby
     * @param password the raw password of the lobby, if applicable
     * @param user     the user that wants to join the lobby
     * @return the joined lobby
     * @throws LobbyNotFoundException      if the lobby was not found
     * @throws WrongLobbyPasswordException if the password was wrong
     */
    public ServerLobby joinLobby(@NonNull UUID lobbyID, @NonNull String password, @NonNull ServerUser user) throws LobbyNotFoundException, WrongLobbyPasswordException {
        Optional<ServerLobby> lobby = lobbyStore.get(lobbyID);
        if (lobby.isPresent()) {
            ServerLobby l = lobby.get();
            if (!l.isPasswordProtected() || passwordEncoder.matches(password, l.getPasswordHash())) {
                joinLobby(lobby.get(), user);
                return lobby.get();
            } else throw new WrongLobbyPasswordException();
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
