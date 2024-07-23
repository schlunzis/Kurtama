package org.schlunzis.kurtama.server.game;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.model.Team;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class GameStore {

    private final Map<UUID, Game> gameMap = new ConcurrentHashMap<>();
    private final IGameFactory gameFactory;

    public Game create(GameSettings gameSettings, List<Team> teams) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (gameMap.containsKey(uuid));

        Game game = gameFactory.create(uuid, gameSettings, teams);
        gameMap.put(uuid, game);
        return game;
    }

    public boolean remove(UUID uuid) {
        return gameMap.remove(uuid) != null;
    }

    public Optional<Game> get(UUID uuid) {
        return Optional.ofNullable(gameMap.get(uuid));
    }

    public Collection<Game> getAll() {
        return gameMap.values();
    }

}
