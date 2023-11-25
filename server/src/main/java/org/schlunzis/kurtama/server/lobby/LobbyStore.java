package org.schlunzis.kurtama.server.lobby;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@NoArgsConstructor
public class LobbyStore {

    private final Map<UUID, ServerLobby> lobbyMap = new HashMap<>();

    public ServerLobby create(String name) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (lobbyMap.containsKey(uuid));

        ServerLobby lobby = new ServerLobby(uuid, name, null); // chatID is set by LobbyManagement
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
