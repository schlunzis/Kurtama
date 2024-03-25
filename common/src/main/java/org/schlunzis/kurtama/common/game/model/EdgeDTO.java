package org.schlunzis.kurtama.common.game.model;

import org.schlunzis.kurtama.common.IUser;

import java.util.List;

/**
 * Represents an edge between two tiles. An edge has a unique id and connects two tiles.
 *
 * @param id              the unique id of the edge
 * @param firstTileIndex  the index of the first tile
 * @param secondTileIndex the index of the second tile
 * @param streets         the list of users that have built a street on this edge
 */
public record EdgeDTO(int id,
                      int firstTileIndex,
                      int secondTileIndex,
                      List<IUser> streets
) {
}
