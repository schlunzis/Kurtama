package org.schlunzis.kurtama.server.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.game.model.SquareGameState;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Game {

    private final UUID id;
    private final SquareGameState gameState;

}
