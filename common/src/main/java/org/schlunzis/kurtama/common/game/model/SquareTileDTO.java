package org.schlunzis.kurtama.common.game.model;

import java.util.List;

public record SquareTileDTO(int id, int[] neighbours, List<TeamDTO> figures) implements ITileDTO {
}
