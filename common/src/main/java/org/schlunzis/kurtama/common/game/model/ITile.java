package org.schlunzis.kurtama.common.game.model;

import java.util.List;

public interface ITile {

    ITile get(IDirection direction);

    void put(IDirection direction, ITile tile);

    List<Team> getFigures();

}

