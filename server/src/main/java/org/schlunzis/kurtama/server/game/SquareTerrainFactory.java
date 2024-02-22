package org.schlunzis.kurtama.server.game;

import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.game.model.ITerrain;
import org.schlunzis.kurtama.common.game.model.SquareDirection;
import org.schlunzis.kurtama.common.game.model.SquareTerrain;
import org.schlunzis.kurtama.common.game.model.SquareTile;

@RequiredArgsConstructor
public class SquareTerrainFactory implements TerrainFactory {

    private final GameSettings gameSettings;

    private SquareTile[][] tiles;
    private int columns;
    private int rows;

    private static boolean isInbound(int index, int upperBound) {
        return index >= 0 && index < upperBound;
    }

    @Override
    public ITerrain create() {
        columns = gameSettings.columns();
        rows = gameSettings.rows();
        tiles = new SquareTile[columns][rows];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new SquareTile();
            }
        }

        linkTiles();

        return new SquareTerrain(tiles);
    }

    private void linkTiles() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                final SquareTile tile = tiles[i][j];
                final int above = j - 1;
                final int left = i - 1;
                final int right = i + 1;
                final int below = j + 1;
                if (isInbound(above, rows)) tile.put(SquareDirection.TOP, tiles[i][above]);
                if (isInbound(left, columns)) tile.put(SquareDirection.LEFT, tiles[i][left]);
                if (isInbound(right, columns)) tile.put(SquareDirection.RIGHT, tiles[i][right]);
                if (isInbound(below, rows)) tile.put(SquareDirection.BOTTOM, tiles[i][below]);
            }
        }
    }

}
