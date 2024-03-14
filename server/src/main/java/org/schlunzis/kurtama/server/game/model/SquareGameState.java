package org.schlunzis.kurtama.server.game.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.schlunzis.kurtama.common.game.model.SquareGameStateDTO;

import java.util.List;

@Getter
@AllArgsConstructor
public class SquareGameState {

    private final SquareTerrain terrain;
    private final List<Team> teams;

    public SquareGameStateDTO toDTO() {
        return new SquareGameStateDTO(terrain.toDTO());
    }

}
