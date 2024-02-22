package org.schlunzis.kurtama.common.game.model;

public class HexagonTerrain implements ITerrain {

    private HexagonTile[][] tiles;

    public HexagonTerrain(int columns, int rows) {
        tiles = new HexagonTile[columns][rows];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new HexagonTile();
            }
        }
    }

    public ITile get(int index) {
        return null;
    }

}
