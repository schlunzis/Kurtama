package org.schlunzis.kurtama.server.game.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.game.model.ITileDTO;
import org.schlunzis.kurtama.common.game.model.SquareTileDTO;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class SquareTile implements ITile {

    @Getter
    private final int id;

    private final Edge[] neighbours = new Edge[SquareDirection.values().length];
    @Getter
    private final List<ServerUser> figures = new ArrayList<>();
    @Getter
    private final List<Team> villages = new ArrayList<>();
    @Getter
    private final List<Team> cities = new ArrayList<>();
    @Getter
    private final List<Team> climateCities = new ArrayList<>();

    @Override
    public ITile get(IDirection direction) {
        return neighbours[direction.getIndex()].next();
    }

    @Override
    public void put(IDirection direction, Edge edge) {
        if (neighbours[direction.getIndex()] != null)
            throw new IllegalStateException("Tile is already set!");
        neighbours[direction.getIndex()] = edge;
    }

    @Override
    public ITileDTO toDTO() {
        int[] neighbourIds = new int[neighbours.length];
        for (int i = 0; i < neighbours.length; i++) {
            neighbourIds[i] = neighbours[i] == null ? -1 : neighbours[i].next().getId();
        }
        List<IUser> figureDTOs = new ArrayList<>();
        for (ServerUser figure : figures) {
            figureDTOs.add(figure.toDTO());
        }
        return new SquareTileDTO(id, neighbourIds, figureDTOs);
    }

    public boolean hasFigureOfUser(ServerUser user) {
        return figures.contains(user);
    }

}
