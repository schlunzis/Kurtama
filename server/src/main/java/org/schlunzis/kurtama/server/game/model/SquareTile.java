package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SquareTile implements ITile {

    private final ITile[] neighbours = new ITile[SquareDirection.values().length];
    @Getter
    private final List<Team> figures = new ArrayList<>();
    @Getter
    private final List<Team> villages = new ArrayList<>();
    @Getter
    private final List<Team> cities = new ArrayList<>();
    @Getter
    private final List<Team> climateCities = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private final List<Team>[] streets = (List<Team>[]) Array.newInstance(List.class, SquareDirection.values().length);

    public SquareTile() {
        for (int i = 0; i < streets.length; i++) {
            streets[i] = new ArrayList<>();
        }
    }

    @Override
    public ITile get(IDirection direction) {
        return neighbours[direction.getIndex()];
    }

    @Override
    public void put(IDirection direction, ITile tile) {
        if (neighbours[direction.getIndex()] != null)
            throw new IllegalStateException("Tile is already set!");
        neighbours[direction.getIndex()] = tile;
    }

    @Override
    public List<Team> getStreets(IDirection direction) {
        return streets[direction.getIndex()];
    }

    @Override
    public void addStreet(Team team, IDirection direction) {
        streets[direction.getIndex()].add(team);
    }

    @Override
    public void clearStreets() {
        for (List<Team> teamList : streets)
            teamList.clear();
    }

}
