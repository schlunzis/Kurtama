package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SquareTile implements ITile {

    private final ITile[] neighbours = new ITile[SquareDirection.values().length];
    @Getter
    private final List<Team> figures = new ArrayList<>();

    @Override
    public ITile get(IDirection direction) {
        return neighbours[direction.getIndex()];
    }

    @Override
    public void put(IDirection direction, ITile tile) {
        if (neighbours[direction.getIndex()] != null)
            throw new IllegalStateException("Tile is already set!");
        neighbours[direction.getIndex()] = tile;
    }

}
