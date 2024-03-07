package org.schlunzis.kurtama.server.game.model;

public class GenericTerrain<D extends IDirection> implements ITerrain<D> {

    private final ITile<D>[] tiles;

    public GenericTerrain(ITile<D>[] tiles) {
        this.tiles = tiles;
    }

    @Override
    public ITile<D> get(int index) {
        return tiles[index];
    }

}
