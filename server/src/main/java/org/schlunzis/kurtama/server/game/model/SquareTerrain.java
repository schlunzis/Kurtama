package org.schlunzis.kurtama.server.game.model;

import org.schlunzis.kurtama.common.game.model.ITerrainDTO;
import org.schlunzis.kurtama.common.game.model.ITileDTO;
import org.schlunzis.kurtama.common.game.model.SquareTerrainDTO;
import org.schlunzis.kurtama.common.game.model.SquareTileDTO;
import org.schlunzis.kurtama.server.user.ServerUser;

public class SquareTerrain implements ITerrain {

    private final SquareTile[][] tiles;
    private final int columns;
    private final int rows;

    public SquareTerrain(SquareTile[][] tiles) {
        this.tiles = tiles;
        this.columns = tiles.length;
        this.rows = tiles[0].length;
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
        return new SquareTerrainDTO(columns, rows, (SquareTileDTO[][]) tileDTOs);
    }

    public SquareTile findTileWithFigureOfUser(ServerUser user) {
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                if (tiles[x][y].hasFigureOfUser(user)) {
                    return tiles[x][y];
                }
            }
        }
        return null;
    }

}
