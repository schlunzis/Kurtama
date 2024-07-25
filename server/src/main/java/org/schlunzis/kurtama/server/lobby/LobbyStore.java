package org.schlunzis.kurtama.server.lobby;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@NoArgsConstructor
public class LobbyStore {

    private final Map<UUID, ServerLobby> lobbyMap = new HashMap<>();

    /**
     * Create a new lobby with a random UUID.
     *
     * @param name         the name of the lobby
     * @param passwordHash the password hash of the lobby
     * @return the created lobby
     */
    public ServerLobby create(String name, String passwordHash) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (lobbyMap.containsKey(uuid));

        ServerLobby lobby = new ServerLobby(uuid, name, passwordHash, null, null); // chatID and owner are set by LobbyManagement
        lobbyMap.put(uuid, lobby);
        return lobby;
    }

    public boolean remove(UUID uuid) {
        return lobbyMap.remove(uuid) != null;
    }

    public Optional<ServerLobby> get(UUID uuid) {
        return Optional.ofNullable(lobbyMap.get(uuid));
    }

    public Collection<ServerLobby> getAll() {
        return lobbyMap.values();
    }

}
