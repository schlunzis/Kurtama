package org.schlunzis.kurtama.common.game.model;

import java.util.List;

public record SquareGameStateDTO(ITerrainDTO terrain, List<TeamDTO> teams) implements IGameStateDTO {
}
