package org.schlunzis.kurtama.common.game.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record SquareTerrainDTO(int width,
                               int height,
                               SquareTileDTO[][] tiles,
                               List<EdgeDTO> edges
) implements ITerrainDTO {

    @Override
    public String toString() {
        return "SquareTerrainDTO{" +
                "width=" + width +
                ", height=" + height +
                ", tiles=" + Arrays.toString(tiles) +
                ", edges=" + edges +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SquareTerrainDTO that = (SquareTerrainDTO) o;
        return width == that.width && height == that.height && Arrays.deepEquals(tiles, that.tiles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.deepHashCode(tiles);
        return result;
    }

}
