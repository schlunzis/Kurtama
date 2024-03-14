package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;

@Getter
public enum HexagonDirection implements IDirection {

    TOP,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
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
