package org.schlunzis.kurtama.server.game;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GameManagement {

    private final GameStore gameStore;

    public Game createGame(GameSettings gameSettings) {
        return gameStore.create(gameSettings);
    }

    public void removeGame(Game game) {
        gameStore.remove(game.getId());
    }

    public void removeGame(UUID id) {
        gameStore.remove(id);
    }

    public Game getGame(UUID id) {
        return gameStore.get(id).orElseThrow();
    }

}
