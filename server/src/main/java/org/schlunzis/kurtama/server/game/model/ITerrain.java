package org.schlunzis.kurtama.server.game.model;

public interface ITerrain<D extends IDirection> {

    ITile<D> get(int index);

}
