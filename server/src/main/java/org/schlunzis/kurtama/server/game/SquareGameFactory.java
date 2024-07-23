package org.schlunzis.kurtama.server.game;

import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.game.model.SquareTerrain;
import org.schlunzis.kurtama.server.game.model.Team;
import org.schlunzis.kurtama.server.game.model.factory.SquareTerrainFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class SquareGameFactory implements IGameFactory {

    @Override
    public Game create(UUID id, GameSettings gameSettings, List<Team> teams) {
        SquareGameState gameState = createGameState(gameSettings);
        Game game = new Game(id, gameState);
        game.getTeams().addAll(teams);
        return game;
    }

    private SquareGameState createGameState(GameSettings gameSettings) {
        SquareTerrainFactory terrainFactory = new SquareTerrainFactory(gameSettings);
        return new SquareGameState((SquareTerrain) terrainFactory.create(), Collections.emptyList());
    }

}
