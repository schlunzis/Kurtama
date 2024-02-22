package org.schlunzis.kurtama.common.game.model;

public class SquareTerrain implements ITerrain {

    private final SquareTile[][] tiles;
    private final int columns;
    private final int rows;

    public SquareTerrain(SquareTile[][] tiles) {
        this.tiles = tiles;
        this.columns = tiles.length;
        this.rows = tiles[0].length;
    }

    public ITile get(int index) {
        return tiles[index % columns][index / columns];
    }

}
