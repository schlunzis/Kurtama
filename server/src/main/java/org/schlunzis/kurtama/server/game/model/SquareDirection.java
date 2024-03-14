package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;

@Getter
public enum SquareDirection implements IDirection {

    TOP,
    LEFT,
    RIGHT,
    BOTTOM;

    @Override
    public IDirection[] directions() {
        return values();
    }

    @Override
    public int getIndex() {
        return ordinal();
    }

}
