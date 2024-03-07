package org.schlunzis.kurtama.server.game.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SquareDirection implements IDirection {

    TOP(0),
    LEFT(1),
    RIGHT(2),
    BOTTOM(3);

    private final int index;

    @Override
    public IDirection[] directions() {
        return values();
    }

}
