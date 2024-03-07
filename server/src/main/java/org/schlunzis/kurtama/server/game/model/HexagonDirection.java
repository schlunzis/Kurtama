package org.schlunzis.kurtama.server.game.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum HexagonDirection implements IDirection {

    TOP(0),
    TOP_LEFT(1),
    TOP_RIGHT(2),
    BOTTOM_LEFT(3),
    BOTTOM_RIGHT(4),
    BOTTOM(5);

    private final int index;

    @Override
    public IDirection[] directions() {
        return values();
    }

}
