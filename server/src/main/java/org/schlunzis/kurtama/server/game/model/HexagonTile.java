package org.schlunzis.kurtama.server.game.model;


import java.util.List;

public class HexagonTile implements ITile {

    private final ITile[] neighbours = new ITile[HexagonDirection.values().length];


    public ITile getTop() {
        return neighbours[HexagonDirection.TOP.getIndex()];
    }

    public ITile getTopLeft() {
        return neighbours[HexagonDirection.TOP_LEFT.getIndex()];
    }

    public ITile getTopRight() {
        return neighbours[HexagonDirection.TOP_RIGHT.getIndex()];
    }

    public ITile getBottomLeft() {
        return neighbours[HexagonDirection.BOTTOM_LEFT.getIndex()];
    }

    public ITile getBottomRight() {
        return neighbours[HexagonDirection.BOTTOM_RIGHT.getIndex()];
    }

    public ITile getBottom() {
        return neighbours[HexagonDirection.BOTTOM.getIndex()];
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
    public List<Team> getFigures() {
        return null;
    }

    @Override
    public List<Team> getStreets(IDirection direction) {
        return null;
    }

    @Override
    public void addStreet(Team team, IDirection direction) {

    }

    @Override
    public void clearStreets() {

    }

}
