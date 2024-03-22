package org.schlunzis.kurtama.common.game.model;

import org.schlunzis.kurtama.common.IUser;

import java.util.List;

public record SquareTileDTO(int id, int[] neighbours, List<IUser> figures) implements ITileDTO {
}
