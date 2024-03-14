package org.schlunzis.kurtama.server.game.model;

import org.schlunzis.kurtama.common.game.model.ITileDTO;

import java.util.List;

public interface ITile {

    int getId();

    ITile get(IDirection direction);

    void put(IDirection direction, ITile tile);

    List<Team> getFigures();

    List<Team> getStreets(IDirection direction);

    void addStreet(Team team, IDirection direction);

    void clearStreets();

    ITileDTO toDTO();

}

