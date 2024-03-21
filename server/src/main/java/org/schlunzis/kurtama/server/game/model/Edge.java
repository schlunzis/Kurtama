package org.schlunzis.kurtama.server.game.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.NoSuchElementException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Edge {

    private final ITile tile;
    private final EdgeData data;

    /**
     * Create a pair of edges with the given parameters. Both edges will reference the same data object.
     *
     * @param id              the id of the edges
     * @param firstTile       the firstTileIndex tile
     * @param firstDirection  the direction, in which the firstTileIndex tile can access the secondTileIndex tile
     * @param secondTile      the secondTileIndex tile
     * @param secondDirection the direction, in which the secondTileIndex tile can access the firstTileIndex tile
     * @return a pair of edges
     */
    public static Pair<Edge, Edge> create(int id, ITile firstTile, IDirection firstDirection, ITile secondTile, IDirection secondDirection) {
        EdgeData data = new EdgeData(id, firstTile, firstDirection, secondTile, secondDirection);
        Edge first = new Edge(firstTile, data);
        Edge second = new Edge(secondTile, data);
        return Pair.of(first, second);
    }

    public ITile next() throws NoSuchElementException {
        if (tile == null)
            throw new NoSuchElementException();
        return tile;
    }

    public boolean hasNext() {
        return tile != null;
    }

    public EdgeData data() {
        return data;
    }

}

