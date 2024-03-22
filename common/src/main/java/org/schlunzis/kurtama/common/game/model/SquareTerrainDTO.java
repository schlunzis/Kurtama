package org.schlunzis.kurtama.common.game.model;

import java.util.List;

public record SquareTerrainDTO(int width,
                               int height,
                               SquareTileDTO[][] tiles,
                               List<EdgeDTO> edges
) implements ITerrainDTO {
}
