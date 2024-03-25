package org.schlunzis.kurtama.server.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.game.model.SquareTile;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Game {

    private final UUID id;
    private final SquareGameState gameState;

    public void move(ServerUser user, int fieldIndex) {
        SquareTile currentTile = gameState.findTileWithFigureOfUser(user);
        currentTile.getFigures().removeIf(figure -> figure.equals(user));
        gameState.getTerrain().get(fieldIndex).getFigures().add(user);
    }

}
