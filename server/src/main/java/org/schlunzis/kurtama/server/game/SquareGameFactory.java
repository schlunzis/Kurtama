package org.schlunzis.kurtama.server.game;

import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.game.model.SquareTerrain;
import org.schlunzis.kurtama.server.game.model.factory.SquareTerrainFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
public class SquareGameFactory implements IGameFactory {

    @Override
    public Game create(UUID id, GameSettings gameSettings) {
        SquareGameState gameState = createGameState(gameSettings);
        return new Game(id, gameState);
    }

    private SquareGameState createGameState(GameSettings gameSettings) {
        SquareTerrainFactory terrainFactory = new SquareTerrainFactory(gameSettings);
        return new SquareGameState((SquareTerrain) terrainFactory.create(), Collections.emptyList());
    }

}
