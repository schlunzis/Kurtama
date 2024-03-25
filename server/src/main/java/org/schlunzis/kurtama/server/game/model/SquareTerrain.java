package org.schlunzis.kurtama.server.game.model;

import org.schlunzis.kurtama.common.game.model.*;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.List;
import java.util.stream.Collectors;

public class SquareTerrain implements ITerrain {

    private final SquareTile[][] tiles;
    private final int columns;
    private final int rows;
    /**
     * Unmodifiable List of all edges in the terrain. This list is used to create the DTO.
     */
    private final List<EdgeData> edgeDataList;

    public SquareTerrain(SquareTile[][] tiles, List<EdgeData> edgeDataList) {
        this.tiles = tiles;
        this.columns = tiles.length;
        this.rows = tiles[0].length;
        this.edgeDataList = edgeDataList;
    }

    public ITile get(int index) {
        return tiles[index % columns][index / columns];
    }

    @Override
    public ITerrainDTO toDTO() {
        ITileDTO[][] tileDTOs = new SquareTileDTO[columns][rows];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                tileDTOs[x][y] = tiles[x][y].toDTO();
            }
        }
        List<EdgeDTO> edgeDTOs = edgeDataList.stream().map(EdgeData::toDTO).toList();
        return new SquareTerrainDTO(columns, rows, (SquareTileDTO[][]) tileDTOs, edgeDTOs);
    }

    public SquareTile findTileWithFigureOfUser(ServerUser user) {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (tiles[x][y].hasFigureOfUser(user)) {
                    return tiles[x][y];
                }
            }
        }
        return tiles[0][0];
    }

}
