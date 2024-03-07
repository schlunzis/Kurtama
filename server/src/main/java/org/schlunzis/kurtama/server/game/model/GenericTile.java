package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GenericTile<D extends IDirection> implements ITile<D> {

    private final ITile<D>[] neighbours;
    @Getter
    private final List<Team> figures = new ArrayList<>();
    @Getter
    private final List<Team> villages = new ArrayList<>();
    @Getter
    private final List<Team> cities = new ArrayList<>();
    @Getter
    private final List<Team> climateCities = new ArrayList<>();
    /**
     * List of streets indexed by direction.
     */
    private final List<Team>[] streets;

    public GenericTile(D instance) {
        //noinspection unchecked
        neighbours = (ITile<D>[]) Array.newInstance(instance.getClass().getComponentType(), instance.directions().length);
        //noinspection unchecked
        streets = (List<Team>[]) Array.newInstance(Team.class.getComponentType(), instance.directions().length);
        for (int i = 0; i < streets.length; i++) {
            streets[i] = new ArrayList<>();
        }
    }

    @Override
    public ITile<D> get(D direction) {
        return neighbours[direction.getIndex()];
    }

    @Override
    public void put(D direction, ITile<D> tile) {
        if (neighbours[direction.getIndex()] != null)
            throw new IllegalStateException("Tile is already set!");
        neighbours[direction.getIndex()] = tile;
    }

    @Override
    public List<Team> getStreets(D direction) {
        return streets[direction.getIndex()];
    }

    @Override
    public void addStreet(Team team, D direction) {
        streets[direction.getIndex()].add(team);
    }

    @Override
    public void clearStreets() {
        for (List<Team> teamList : streets)
            teamList.clear();
    }

}
