package org.schlunzis.kurtama.server.lobby;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@NoArgsConstructor
public class LobbyStore {

    private final Map<UUID, ServerLobby> lobbyMap = new HashMap<>();

    public ServerLobby create(String name) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (lobbyMap.containsKey(uuid));

        ServerLobby lobby = new ServerLobby(uuid, name);
        lobbyMap.put(uuid, lobby);
        return lobby;
    }

    public boolean remove(UUID uuid) {
        return lobbyMap.remove(uuid) != null;
    }

    // TODO make this an optional and handle errors accordingly
    public ServerLobby get(UUID uuid) {
        return lobbyMap.get(uuid);
    }

    public Collection<ServerLobby> getAll() {
        return lobbyMap.values();
    }

}
