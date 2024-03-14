package org.schlunzis.kurtama.common.game.model;

import java.util.Arrays;
import java.util.Objects;

public record SquareTileDTO(int id, int[] neighbours) implements ITileDTO {

    @Override
    public String toString() {
        return "SquareTileDTO{" +
                "id=" + id +
                ", neighbours=" + Arrays.toString(neighbours) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SquareTileDTO that = (SquareTileDTO) o;
        return id == that.id && Arrays.equals(neighbours, that.neighbours);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(neighbours);
        return result;
    }
}
