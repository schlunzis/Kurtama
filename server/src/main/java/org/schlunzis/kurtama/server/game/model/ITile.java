package org.schlunzis.kurtama.server.game.model;

import java.util.List;

public interface ITile<D extends IDirection> {

    ITile<D> get(D direction);

    void put(D direction, ITile<D> tile);

    List<Team> getFigures();

    List<Team> getStreets(D direction);

    void addStreet(Team team, D direction);

    void clearStreets();

}

