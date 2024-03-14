package org.schlunzis.kurtama.server.game;

class GenericSquareTerrainFactory {

}

/*@RequiredArgsConstructor
public class GenericSquareTerrainFactory implements TerrainFactory {

    private final GameSettings gameSettings;

    private ITile<SquareDirection>[][] tiles;
    private int columns;
    private int rows;

    private static boolean isInbound(int index, int upperBound) {
        return index >= 0 && index < upperBound;
    }

    @Override
    public ITerrain<SquareDirection> create() {
        columns = gameSettings.columns();
        rows = gameSettings.rows();
        //noinspection unchecked
        tiles = (ITile<SquareDirection>[][]) Array.newInstance(ITile.class, columns, rows);

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new GenericTile<>(SquareDirection.TOP);
            }
        }

        linkTiles();
        ITile<SquareDirection>[] flatTiles = flattenArray(tiles);

        return new GenericTerrain<>(flatTiles, columns, rows);
    }

    private ITile<SquareDirection>[] flattenArray(ITile<SquareDirection>[][] tiles) {
        //noinspection unchecked
        ITile<SquareDirection>[] flatTiles = (ITile<SquareDirection>[]) Array.newInstance(ITile.class, columns * rows);
        int index = 0;
        for (ITile<SquareDirection>[] tileArray : tiles) {
            System.arraycopy(tileArray, 0, flatTiles, index, tileArray.length);
            index += tileArray.length;
        }
        return flatTiles;
    }

    private void linkTiles() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                final ITile<SquareDirection> tile = tiles[i][j];
                final int above = j - 1;
                final int left = i - 1;
                final int right = i + 1;
                final int below = j + 1;
                if (isInbound(above, rows)) tile.put(SquareDirection.TOP, tiles[i][above]);
                if (isInbound(left, columns)) tile.put(SquareDirection.LEFT, tiles[left][j]);
                if (isInbound(right, columns)) tile.put(SquareDirection.RIGHT, tiles[right][j]);
                if (isInbound(below, rows)) tile.put(SquareDirection.BOTTOM, tiles[i][below]);
            }
        }
    }

}*/
