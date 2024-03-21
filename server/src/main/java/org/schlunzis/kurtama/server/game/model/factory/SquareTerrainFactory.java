package org.schlunzis.kurtama.server.game.model.factory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.model.*;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SquareTerrainFactory implements TerrainFactory {

    private final GameSettings gameSettings;

    private final List<TilesWithDirections> tilesWithDirections = new ArrayList<>();
    private final List<EdgeData> edgeDataList = new ArrayList<>();
    private SquareTile[][] tiles;
    private int columns;
    private int rows;
    private int edgeIdCounter = 0;

    private static boolean isInbound(int index, int upperBound) {
        return index >= 0 && index < upperBound;
    }

    @Override
    public ITerrain create() {
        columns = gameSettings.columns();
        rows = gameSettings.rows();
        tiles = new SquareTile[columns][rows];
        int idCounter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[j][i] = new SquareTile(idCounter++);
            }
        }

        linkTiles();

        log.error("EdgeDataList: " + edgeDataList);
        List<EdgeData> edgeData = edgeDataList.stream().distinct().toList();
        log.error("EdgeData: " + edgeData);
        return new SquareTerrain(tiles, edgeData);
    }

    private void linkTiles() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                final SquareTile tile = tiles[i][j];
                final int above = j - 1;
                final int left = i - 1;
                final int right = i + 1;
                final int below = j + 1;
                if (isInbound(above, rows)) registerTilesWithDirection(tile, SquareDirection.TOP, tiles[i][above]);
                if (isInbound(left, columns)) registerTilesWithDirection(tile, SquareDirection.LEFT, tiles[left][j]);
                if (isInbound(right, columns)) registerTilesWithDirection(tile, SquareDirection.RIGHT, tiles[right][j]);
                if (isInbound(below, rows)) registerTilesWithDirection(tile, SquareDirection.BOTTOM, tiles[i][below]);
            }
        }
        tilesWithDirections.forEach(TilesWithDirections::createEdges);
    }

    /**
     * This method creates edges between two tiles. It saves an instance of TilesWithDirections for each tile pair.
     * If the list already contains an instance of TilesWithDirections for the given tile and its direction, the secondTileIndex
     * tile is set with its direction. If the list does not contain an instance of TilesWithDirections for the given
     * tile and its direction, a new instance is created and added to the list.
     *
     * @param tile      the firstTileIndex tile
     * @param direction the direction of the firstTileIndex tile can access the secondTileIndex tile
     * @param otherTile the secondTileIndex tile
     */
    private void registerTilesWithDirection(SquareTile tile, IDirection direction, SquareTile otherTile) {
        Optional<TilesWithDirections> optionalTilesWithDirections = tilesWithDirections.stream()
                .filter(twd -> twd.getSecond().equals(otherTile) && twd.getFirst().equals(tile))
                .findFirst();

        if (optionalTilesWithDirections.isPresent()) {
            TilesWithDirections twd = optionalTilesWithDirections.get();
            twd.setSecondDirection(direction);
        } else {
            tilesWithDirections.add(new TilesWithDirections(tile, direction, otherTile));
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    private class TilesWithDirections {

        private SquareTile first;
        private IDirection firstDirection;
        private SquareTile second;
        private IDirection secondDirection;

        public TilesWithDirections(SquareTile first, IDirection firstDirection, SquareTile second) {
            this.first = first;
            this.firstDirection = firstDirection;
            this.second = second;
        }

        public void createEdges() {
            Pair<Edge, Edge> edges = Edge.create(edgeIdCounter++, first, firstDirection, second, secondDirection);
            first.put(firstDirection, edges.getSecond());
            if (secondDirection != null) second.put(secondDirection, edges.getFirst());
            edgeDataList.add(edges.getFirst().data());
        }
    }

}
